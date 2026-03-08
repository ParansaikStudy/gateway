# ZQKSK Gateway 배포 가이드라인

## 목차
1. [아키텍처 구성](#1-아키텍처-구성)
2. [사전 요구사항](#2-사전-요구사항)
3. [서버 환경 설정](#3-서버-환경-설정)
4. [Docker 설치](#4-docker-설치)
5. [Jenkins 설치](#5-jenkins-설치)
6. [프로젝트 배포](#6-프로젝트-배포)
7. [서비스 실행](#7-서비스-실행)
8. [Jenkins 설정](#8-jenkins-설정)
9. [모니터링 및 로그](#9-모니터링-및-로그)
10. [트러블슈팅](#10-트러블슈팅)
11. [서버 종료 및 재시작](#11-서버-종료-및-재시작)

---

## 1. 아키텍처 구성

```
┌─────────────────────────────────────────────────────────────┐
│              192.168.45.87 Server (ODROID armv7l)           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   ┌─────────────────── Docker Container ──────────────────┐ │
│   │                                                        │ │
│   │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │ │
│   │  │   Jenkins    │  │   MariaDB    │  │   Eureka     │ │ │
│   │  │    :8080     │  │    :3310     │  │    :7001     │ │ │
│   │  │   (CI/CD)    │  │     (DB)     │  │ (Discovery)  │ │ │
│   │  │   [768MB]    │  │   [512MB]    │  │   [512MB]    │ │ │
│   │  └──────────────┘  └──────────────┘  └──────────────┘ │ │
│   │                                                        │ │
│   │  ┌──────────────┐  ┌──────────────┐                   │ │
│   │  │     Auth     │  │   Gateway    │                   │ │
│   │  │    :7002     │  │    :7003     │                   │ │
│   │  │  (인증서비스)  │  │ (API Gateway)│                   │ │
│   │  │   [512MB]    │  │   [512MB]    │                   │ │
│   │  └──────────────┘  └──────────────┘                   │ │
│   │                                                        │ │
│   │  Docker Network: zqksk-network                        │ │
│   └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 구성 요소

| 구성 요소 | 실행 방식 | 이미지/버전 | 포트 | 메모리 제한 |
|----------|----------|-------------|------|------------|
| Jenkins | Docker 컨테이너 | arm32v7/ubuntu:22.04 + JDK21 | 8080 | 768MB |
| MariaDB | Docker 컨테이너 | yobasystems/alpine-mariadb:arm32v7 | 3310 | 512MB |
| Discovery | Docker 컨테이너 | arm32v7/ubuntu:22.04 + JDK21 | 7001 | 512MB |
| Auth | Docker 컨테이너 | arm32v7/ubuntu:22.04 + JDK21 | 7002 | 512MB |
| Gateway | Docker 컨테이너 | arm32v7/ubuntu:22.04 + JDK21 | 7003 | 512MB |

> **참고**: 오드로이드 RAM 부족 방지를 위해 각 컨테이너에 메모리 제한이 설정되어 있습니다. (총 약 2.8GB)

---

## 2. 사전 요구사항

### 서버 사양
- **OS**: Ubuntu 22.04 LTS
- **Architecture**: armv7l (32-bit ARM)
- **CPU**: 4 Core 이상
- **RAM**: 4GB 이상
- **Disk**: 32GB 이상

### 네트워크 요구사항
- 서버 IP: 192.168.45.87
- 필요 포트: 8080, 3310, 7001, 7002, 7003

---

## 3. 서버 환경 설정

### 3.1 시스템 업데이트

```bash
sudo apt update && sudo apt upgrade -y
```

### 3.2 JDK 21 설치

```bash
sudo apt install -y openjdk-21-jdk

# 설치 확인
java -version
# openjdk version "21.0.9" 2025-10-21
```

### 3.3 타임존 설정

```bash
sudo timedatectl set-timezone Asia/Seoul
```

### 3.4 방화벽 설정

```bash
# UFW 활성화
sudo ufw enable

# 필요 포트 열기
sudo ufw allow 22/tcp     # SSH
sudo ufw allow 8080/tcp   # Jenkins
sudo ufw allow 3310/tcp   # MariaDB
sudo ufw allow 7001/tcp   # Eureka
sudo ufw allow 7002/tcp   # Auth
sudo ufw allow 7003/tcp   # Gateway

# 상태 확인
sudo ufw status
```

---

## 4. Docker 설치

### 4.1 Docker Engine 설치

```bash
# 필수 패키지 설치
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

# Docker GPG 키 추가
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# Docker 저장소 추가
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker 설치
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Docker 서비스 시작 및 자동 시작 설정
sudo systemctl start docker
sudo systemctl enable docker
```

### 4.2 Docker Compose V2 플러그인 설치

```bash
# Docker Compose V2 플러그인 설치 (권장)
sudo apt-get install -y docker-compose-plugin

# 또는 수동 설치
sudo mkdir -p /usr/local/lib/docker/cli-plugins
sudo curl -SL "https://github.com/docker/compose/releases/latest/download/docker-compose-linux-armv7" -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
```

> **참고**: Docker Compose V2부터 `docker-compose` (하이픈) 대신 `docker compose` (공백)를 사용합니다.

### 4.3 현재 사용자에게 Docker 권한 부여

```bash
sudo usermod -aG docker $USER
newgrp docker
```

### 4.4 설치 확인

```bash
docker --version
docker compose version
docker run hello-world
```

---

## 5. Jenkins 설치

Jenkins는 Docker 컨테이너로 실행됩니다. docker-compose.yml에 이미 설정되어 있습니다.

### 5.1 기존 호스트 Jenkins 종료 (설치되어 있는 경우)

```bash
# systemd 서비스 중지 및 비활성화
sudo systemctl stop jenkins
sudo systemctl disable jenkins

# 또는 수동 실행 프로세스 종료
pkill -f jenkins.war
```

### 5.2 Docker로 Jenkins 실행

```bash
cd /root/gateway-dev

# Jenkins 컨테이너만 빌드 및 실행
docker compose up -d --build jenkins

# 로그 확인
docker compose logs -f jenkins
```

### 5.3 Jenkins 초기 비밀번호 확인

```bash
# 컨테이너 내부에서 초기 비밀번호 확인
docker exec zqksk-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### 5.4 Jenkins에서 Docker 사용 설정

Docker 소켓이 이미 마운트되어 있어 별도 설정 없이 Docker 명령어 사용 가능:

```bash
# 컨테이너 내부에서 Docker 테스트
docker exec zqksk-jenkins docker ps
```

### 5.5 Jenkins 볼륨

| 볼륨 | 용도 |
|------|------|
| `jenkins_home` | Jenkins 설정, 플러그인, 작업 데이터 |
| `/var/run/docker.sock` | 호스트 Docker 접근 |
| `/workspace` | 프로젝트 소스코드 |

---

## 6. 프로젝트 배포

### 6.1 프로젝트 디렉토리 생성

```bash
mkdir -p /root/gateway-dev
cd /root/gateway-dev
```

### 6.2 프로젝트 복사

**방법 1: Git Clone (권장)**
```bash
cd /root
git clone https://github.com/your-repo/gateway-dev.git
cd gateway-dev
```

**방법 2: SCP로 복사**
```bash
# 로컬에서 실행
scp -r /path/to/gateway-dev root@192.168.45.87:/root/
```

### 6.3 프로젝트 구조

```
/root/gateway-dev/
├── docker-compose.yml          # Docker Compose 설정
├── Jenkinsfile                 # Jenkins 파이프라인
├── docker/
│   ├── jenkins/
│   │   ├── Dockerfile          # Jenkins 서비스 (JDK 21)
│   │   └── jenkins.war         # Jenkins WAR (로컬에서 다운로드)
│   ├── discovery/Dockerfile    # Eureka 서비스 (JDK 21)
│   ├── auth/Dockerfile         # Auth 서비스 (JDK 21)
│   ├── gateway/Dockerfile      # Gateway 서비스 (JDK 21)
│   └── mariadb/init/           # DB 초기화 스크립트
├── api/
│   ├── discovery/              # Eureka 소스코드
│   ├── auth/                   # Auth 소스코드
│   └── gateway/                # Gateway 소스코드
├── domain/                     # 도메인 모듈
├── storage/                    # 스토리지 모듈
└── support/                    # 지원 모듈
```

---

## 7. 서비스 실행

> **중요**: 오드로이드(ARM 32bit)는 RAM이 부족하여 Gradle 빌드 시 시스템이 멈출 수 있습니다.
> **반드시 로컬(Windows/Mac)에서 빌드 후 JAR 파일을 서버로 전송하세요.**

### 7.1 로컬에서 빌드 (Windows/Mac)

```bash
cd /path/to/gateway-dev

# 전체 빌드
./gradlew clean build -x test

# 또는 개별 빌드
./gradlew :api:discovery:bootJar -x test
./gradlew :api:auth:bootJar -x test
./gradlew :api:gateway:bootJar -x test

# Jenkins WAR 다운로드 (최초 1회)
cd docker/jenkins
curl -O https://get.jenkins.io/war-stable/latest/jenkins.war
```

### 7.2 서버로 프로젝트 전송

```bash
# 방법 1: 전체 프로젝트 전송 (빌드된 JAR 포함)
scp -r ./gateway-dev root@192.168.45.87:/root/

# 방법 2: JAR 파일만 전송 (프로젝트가 이미 서버에 있는 경우)
scp api/discovery/build/libs/*.jar root@192.168.45.87:/root/gateway-dev/api/discovery/build/libs/
scp api/auth/build/libs/*.jar root@192.168.45.87:/root/gateway-dev/api/auth/build/libs/
scp api/gateway/build/libs/*.jar root@192.168.45.87:/root/gateway-dev/api/gateway/build/libs/
```

### 7.3 Docker 컨테이너 실행 (서버에서)

```bash
cd /root/gateway-dev

# 순차적 실행 (권장)
docker compose up -d mariadb
docker compose up -d discovery
docker compose up -d auth
docker compose up -d gateway
docker compose up -d jenkins

# 또는 전체 실행
docker compose up -d --build
```

### 7.4 컨테이너 상태 확인

```bash
docker compose ps
```

예상 결과:
```
NAME               IMAGE                                STATUS          PORTS
zqksk-jenkins      gateway-dev-jenkins                  Up              0.0.0.0:8080->8080/tcp
zqksk-mariadb      yobasystems/alpine-mariadb:arm32v7   Up (healthy)    0.0.0.0:3310->3306/tcp
zqksk-discovery    gateway-dev-discovery                Up (healthy)    0.0.0.0:7001->7001/tcp
zqksk-auth         gateway-dev-auth                     Up              0.0.0.0:7002->7002/tcp
zqksk-gateway      gateway-dev-gateway                  Up              0.0.0.0:7003->7003/tcp
```

### 7.5 서비스 접속 확인

| 서비스 | URL | 설명 |
|--------|-----|------|
| Jenkins | http://192.168.45.87:8080 | CI/CD 대시보드 |
| Eureka | http://192.168.45.87:7001 | 서비스 디스커버리 대시보드 |
| Auth | http://192.168.45.87:7002/actuator/health | 헬스체크 |
| Gateway | http://192.168.45.87:7003/actuator/health | 헬스체크 |

---

## 8. Jenkins 설정

### 8.1 Jenkins 초기 설정

1. http://192.168.45.87:8080 접속
2. 초기 비밀번호 입력 (`sudo cat /var/lib/jenkins/secrets/initialAdminPassword`)
3. "Install suggested plugins" 선택
4. 관리자 계정 생성

### 8.2 필수 플러그인 설치

Jenkins 관리 > Plugins > Available plugins에서 설치:
- Docker Pipeline
- Pipeline
- Git
- GitHub Integration

### 8.3 GitHub Personal Access Token(PAT) 설정

깃허브는 보안상 아이디/비밀번호만으로는 접속을 막아두었기 때문에, **Personal Access Token(PAT)**을 만들어서 젠킨스에 등록해야 합니다.

#### GitHub에서 토큰 만들기

1. GitHub 로그인 후 오른쪽 상단 프로필 클릭 → **Settings**
2. 왼쪽 맨 아래 **Developer settings** 클릭
3. **Personal access tokens** → **Tokens (classic)** 클릭
4. **Generate new token (classic)** 선택
5. Note에 `jenkins-key`라고 적고, **repo** 체크박스에 체크한 뒤 맨 아래에서 **Generate token** 클릭
6. 나오는 문자열(`ghp_...`)을 꼭 복사해서 따로 저장해두세요! (한 번만 보여줍니다.)

#### Jenkins에 Credential 등록

1. Jenkins 관리 → **Credentials** → **System** → **Global credentials**
2. **Add Credentials** 클릭
3. 설정:
   - Kind: **Username with password**
   - Username: GitHub 사용자명
   - Password: 위에서 복사한 토큰 (`ghp_...`)
   - ID: `github-token` (파이프라인에서 참조할 이름)
4. **Create** 클릭

### 8.4 파이프라인 생성

1. "새로운 Item" 클릭
2. "Pipeline" 선택
3. Pipeline 설정:
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: (프로젝트 Git URL)
   - Credentials: 위에서 등록한 `github-token` 선택
   - Script Path: Jenkinsfile

### 8.5 Jenkins 작업 디렉토리 설정

```bash
# Jenkins가 프로젝트에 접근할 수 있도록 권한 설정
sudo chown -R jenkins:jenkins /root/gateway-dev
# 또는 Jenkins workspace 사용
```

---

## 9. 모니터링 및 로그

### 9.1 Docker 로그 확인

```bash
# 전체 로그
docker compose logs -f

# 특정 서비스 로그
docker compose logs -f discovery
docker compose logs -f auth
docker compose logs -f gateway
docker compose logs -f mariadb
```

### 9.2 JAR 실행 로그 확인

```bash
tail -f /root/gateway-dev/logs/discovery.log
tail -f /root/gateway-dev/logs/auth.log
tail -f /root/gateway-dev/logs/gateway.log
```

### 9.3 Jenkins 로그 확인

```bash
sudo journalctl -u jenkins -f
```

### 9.4 컨테이너 리소스 모니터링

```bash
docker stats
```

### 9.5 Eureka 대시보드

http://192.168.45.87:7001 에서 등록된 서비스 확인:
- ZQKSK-AUTH-SERVICE
- ZQKSK-API-GATEWAY

---

## 10. 트러블슈팅

### 10.1 컨테이너가 시작되지 않을 때

```bash
# 로그 확인
docker compose logs [서비스명]

# 컨테이너 재시작
docker compose restart [서비스명]

# 전체 재시작
docker compose down
docker compose up -d --build
```

### 10.2 MariaDB 연결 실패

```bash
# MariaDB 컨테이너 접속
docker exec -it zqksk-mariadb mysql -u ldk -p
# 비밀번호: 1q2w3e!Q@W#E
```

### 10.3 Eureka에 서비스가 등록되지 않을 때

1. Discovery 서비스가 먼저 실행되었는지 확인
2. 각 서비스의 eureka 설정 확인
3. 네트워크 연결 확인:
```bash
docker network inspect gateway-dev_zqksk-network
```

### 10.4 빌드 실패 시

```bash
# Gradle 캐시 삭제 후 재빌드
./gradlew clean build -x test --no-daemon

# Docker 캐시 삭제 후 재빌드
docker compose build --no-cache
```

### 10.5 디스크 공간 부족

```bash
# 사용하지 않는 Docker 리소스 정리
docker system prune -a

# 볼륨까지 정리 (주의: 데이터 삭제됨)
docker system prune -a --volumes
```

### 10.6 Jenkins가 Docker 명령어 실행 실패

**Docker 컨테이너 방식 (현재 설정)**:
```bash
# Docker 소켓 권한 확인
ls -la /var/run/docker.sock

# 컨테이너 내부에서 Docker 테스트
docker exec zqksk-jenkins docker ps

# 권한 문제 시 호스트에서 소켓 권한 변경
sudo chmod 666 /var/run/docker.sock
```

**호스트 직접 실행 방식 (이전 설정)**:
```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### 10.7 ARM v7 Docker 관련 에러

```bash
# iptables 에러 발생 시
sudo update-alternatives --set iptables /usr/sbin/iptables-legacy
sudo update-alternatives --set ip6tables /usr/sbin/ip6tables-legacy
sudo systemctl restart docker
```

### 10.8 오드로이드에서 빌드 시 시스템 멈춤 (System Hang)

**증상**:
- `docker-compose up --build` 또는 `./gradlew build` 실행 시 시스템이 멈춤
- SSH 접속 불가
- 키보드/마우스 입력 무응답

**원인**:
- Gradle 빌드는 RAM을 많이 사용 (최소 2GB 이상 권장)
- 오드로이드(ARM 32bit)는 RAM이 부족하여 스왑(Swap) 지옥에 빠짐
- 시스템이 디스크를 메모리처럼 사용하면서 극심한 성능 저하 발생

**해결 방법**:

1. **물리적 재부팅**: 전원을 뺐다가 다시 연결

2. **로컬에서 빌드 후 JAR만 전송** (권장):
```bash
# 로컬(Windows/Mac)에서 빌드
cd gateway-dev
./gradlew clean build -x test

# 서버로 전송
scp -r . root@192.168.45.87:/root/gateway-dev/

# 서버에서 Docker 실행 (빌드 없이)
docker compose up -d --build
```

3. **Dockerfile에서 빌드 단계 제거** (이미 적용됨):
```dockerfile
# 변경 전 (서버에서 빌드 - 위험)
FROM ubuntu AS builder
RUN ./gradlew bootJar  

# 변경 후 (JAR만 복사 - 안전)
FROM ubuntu
COPY api/discovery/build/libs/*.jar app.jar
```

**예방책**:
- 오드로이드에서 절대 Gradle 빌드하지 않기
- 빌드는 항상 로컬 PC 또는 CI/CD 서버에서 수행
- JAR 파일만 오드로이드로 전송

### 10.9 Discovery 서비스가 unhealthy 상태일 때

**증상**:
```
Container zqksk-discovery Error dependency discovery failed to start
dependency failed to start: container zqksk-discovery is unhealthy
```

**원인**:
- docker-compose.yml의 healthcheck가 `/actuator/health` 엔드포인트를 호출하는데, actuator가 제대로 설정되지 않음
- `logback-docker.xml` 파일이 없어서 로깅 초기화 실패
- `spring-boot-starter-actuator` 의존성 누락
- `logging.yml`에서 `endpoints.enabled-by-default: false` 설정으로 actuator 엔드포인트 비활성화

**해결 방법**:

1. **actuator 의존성 추가** (`api/discovery/build.gradle.kts`):
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}
```

2. **health 엔드포인트 명시적 활성화** (`api/discovery/src/main/resources/application.yml`):
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      enabled: true
      show-details: always
```

3. **logback-docker.xml 생성** (`support/logging/src/main/resources/logback/logback-docker.xml`)

4. **JAR 재빌드 및 Docker 이미지 재빌드** (중요: 캐시 무효화 필수):
```bash
# 로컬에서 JAR 재빌드
./gradlew :api:discovery:bootJar -x test

# 서버로 전송 후 Docker 이미지 재빌드 (캐시 무시)
docker compose build --no-cache discovery

# 컨테이너 재시작
docker compose down discovery && docker compose up -d discovery

# 정상 동작 확인
docker exec zqksk-discovery curl http://localhost:7001/actuator/health
```

**확인 방법**:
```bash
# healthcheck 상태 확인
docker inspect zqksk-discovery --format='{{json .State.Health}}'

# actuator health 직접 호출
docker exec zqksk-discovery curl http://localhost:7001/actuator/health
# 정상 응답: {"status":"UP"}
```

### 10.10 Jenkins 파이프라인에서 빌드 실패

**증상**:
- Jenkins에서 `./gradlew build` 실행 시 시스템 멈춤
- 빌드 타임아웃

**해결 방법**:

Jenkins 파이프라인을 "빌드 없이 배포만" 하도록 수정하거나, 외부 빌드 서버를 사용하세요.

```groovy
// Jenkinsfile 예시 (빌드 없이 배포만)
pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                sh 'docker compose down || true'
                sh 'docker compose up -d --build'
            }
        }
    }
}
```

**권장 워크플로우**:
```
[개발자 PC] → Git Push → [GitHub] → Webhook → [Jenkins]
                                                    ↓
                                           docker compose up
                                           (JAR은 이미 빌드됨)
```

### 10.11 Jenkins에서 GitHub 인증 실패

**증상**:
```
stderr: remote: Support for password authentication was removed on August 13, 2021.
fatal: Authentication failed for 'https://github.com/...'
```

**원인**:
- GitHub는 2021년 8월 13일부터 비밀번호 인증을 차단
- Personal Access Token(PAT)을 사용해야 함

**해결 방법**:

1. **GitHub에서 토큰 생성**:
   - GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Generate new token (classic) 클릭
   - Note: `jenkins-key`, Scope: `repo` 체크
   - 생성된 토큰(`ghp_...`) 복사 (한 번만 표시됨!)

2. **Jenkins에 Credential 등록**:
   - Jenkins 관리 → Credentials → System → Global credentials
   - Add Credentials 클릭
   - Kind: Username with password
   - Username: GitHub 사용자명
   - Password: 복사한 토큰 (`ghp_...`)
   - ID: `github-token`

3. **파이프라인에서 Credential 사용**:
   - Pipeline 설정에서 Credentials: `github-token` 선택

### 10.12 Docker 빌드 시 타임존 선택 프롬프트에서 멈춤

**증상**:
```
=> [3/4] RUN apt-get update && apt-get install -y ...
=> => # Geographic area:
=> => #   1. Africa      4. Australia  7. Atlantic  10. Pacific  13. Legacy
=> => #   2. America     5. Arctic     8. Europe    11. US
```
빌드가 멈추고 초만 계속 올라감

**원인**:
- `tzdata` 패키지 설치 시 interactive 모드로 타임존 선택을 요구
- Docker 빌드는 interactive 입력을 받을 수 없어서 무한 대기

**해결 방법**:

Dockerfile에서 `DEBIAN_FRONTEND=noninteractive` 환경변수 설정:

```dockerfile
# 타임존 미리 설정 (interactive 프롬프트 방지)
ENV TZ=Asia/Seoul
ENV DEBIAN_FRONTEND=noninteractive

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && apt-get update && apt-get install -y \
    tzdata \
    ...
```

**빌드 재시도**:
```bash
# Ctrl+C로 중단 후 캐시 무시하고 다시 빌드
docker compose build --no-cache jenkins
docker compose up -d jenkins
```

### 10.13 `docker-compose` 명령어가 실행되지 않음

**증상**:
```
docker-compose: command not found
```
또는 `docker-compose up` 명령어가 실행되지 않음

**원인**:
- Docker Compose V2부터 `docker-compose` (하이픈)가 `docker compose` (공백)로 변경됨
- 최신 Docker에서는 Compose가 별도 바이너리가 아닌 Docker CLI 플러그인으로 통합됨

**해결 방법**:

1. **명령어 변경** (권장):
```bash
# 변경 전 (V1 - 더 이상 사용하지 않음)
docker-compose up -d

# 변경 후 (V2 - 현재 표준)
docker compose up -d
```

2. **Docker Compose V2 플러그인 설치**:
```bash
# apt 패키지로 설치
sudo apt-get install -y docker-compose-plugin

# 설치 확인
docker compose version
```

3. **호환성 심볼릭 링크 생성** (기존 스크립트 호환용):
```bash
# docker-compose → docker compose 별칭 생성
echo 'alias docker-compose="docker compose"' >> ~/.bashrc
source ~/.bashrc
```

> **참고**: 2023년 7월부터 Docker Compose V1은 더 이상 업데이트되지 않습니다.
> 모든 새 프로젝트는 `docker compose` (공백)를 사용하세요.

---

### 10.14 Docker 빌드 시 JAR 파일 없음 (COPY failed: no source files were specified)

**증상**:
```
Step 7/9 : COPY api/discovery/build/libs/*.jar app.jar
COPY failed: no source files were specified
script returned exit code 1
```

**원인**:
- 오드로이드에서 Gradle 빌드가 불가능 (메모리 부족)
- Jenkins가 Git에서 checkout한 코드에 JAR 파일이 없음
- `.gitignore`에서 `build/` 폴더가 제외되어 있음

**해결 방법**:

1. **`.gitignore` 수정** - JAR 파일 예외 추가:
```gitignore
### JAR files for deployment (오드로이드 빌드 불가로 Git에 포함) ###
!api/*/build/
!api/*/build/libs/
!api/*/build/libs/*.jar
```

2. **로컬에서 빌드 후 JAR 파일 Git에 포함**:0
```bash
# 빌드
./gradlew clean build -x test

# JAR 파일 Git에 추가
git add -f api/discovery/build/libs/*.jar
git add -f api/auth/build/libs/*.jar
git add -f api/gateway/build/libs/*.jar

# 커밋 & 푸시
git commit -m "build: JAR 파일 추가"
git push
```

**이후 워크플로우**:
```
코드 수정 → 로컬 빌드 → git push (JAR 포함) → Jenkins Build Now
```

---

## 11. 서버 종료 및 재시작

### 11.1 서버 종료 전 체크리스트
```bash
cd /root/gateway-dev

# 1. 컨테이너 정상 종료 (권장)
docker compose stop
docker stop zqksk-gateway zqksk-auth zqksk-discovery zqksk-mariadb


# 2. 종료 확인
docker ps -a
# 모든 컨테이너가 Exited 상태인지 확인

# 3. 서버 종료
sudo shutdown -h now
```

> **참고**: `docker compose stop`을 하지 않아도 서버가 종료되면서 컨테이너도 함께 종료됩니다.
> 하지만 데이터 무결성을 위해 MariaDB 등은 정상 종료하는 것이 안전합니다.

**급하게 종료해야 할 때:**
```bash
# 그냥 서버 종료 (컨테이너가 강제 종료됨)
sudo shutdown -h now
```

### 11.2 자동 시작 설정 현황

| 서비스 | 자동 시작 | 설정 |
|--------|----------|------|
| Docker | O | `systemctl enable docker` |
| Jenkins 컨테이너 | O | `restart: unless-stopped` |
| MariaDB 컨테이너 | O | `restart: unless-stopped` |
| Discovery 컨테이너 | O | `restart: unless-stopped` |
| Auth 컨테이너 | O | `restart: unless-stopped` |
| Gateway 컨테이너 | O | `restart: unless-stopped` |

> **참고**: `restart: unless-stopped`는 Docker 데몬 시작 시 컨테이너를 자동으로 시작합니다.
> 단, `docker stop`으로 명시적으로 중지한 경우에는 재시작하지 않습니다.

### 11.3 서버 재시작 후 확인 절차

서버 전원을 껐다 켠 후 다음 순서로 확인하세요:

```bash
# 1. Docker 서비스 상태 확인
sudo systemctl status docker

# 2. Jenkins 서비스 상태 확인
sudo systemctl status jenkins

# 3. 컨테이너 상태 확인
docker ps -a

# 4. 모든 컨테이너가 정상 실행 중인지 확인
docker compose ps
```

### 11.4 컨테이너가 자동 시작되지 않은 경우

의존성 순서 문제로 일부 컨테이너가 시작되지 않을 수 있습니다:

```bash
cd /root/gateway-dev

# 전체 서비스 시작
docker compose up -d

# 또는 순차적으로 시작 (더 안정적)
docker compose up -d mariadb
docker compose up -d discovery
docker compose up -d auth
docker compose up -d gateway
docker compose up -d jenkins
```

### 11.5 서비스 정상 동작 확인

```bash
# MariaDB 연결 확인
docker exec zqksk-mariadb mysqladmin ping -uroot -proot1234!

# Discovery health 확인
curl http://localhost:7001/actuator/health

# Auth health 확인 (Eureka 대시보드에서도 확인 가능)
curl http://localhost:7002/actuator/health

# Gateway health 확인
curl http://localhost:7003/actuator/health

# Eureka 대시보드 접속
# http://192.168.45.87:7001
```

### 11.6 문제 발생 시 전체 재시작

```bash
cd /root/gateway-dev

# 모든 컨테이너 중지 및 삭제
docker compose down

# 전체 서비스 재시작
docker compose up -d
```




------------------------------------------------------------------------------------------------

## 부록

### A. 유용한 명령어

```bash
# 모든 컨테이너 중지
docker compose stop

# 모든 컨테이너 삭제 (볼륨 유지)
docker compose down

# 모든 컨테이너 삭제 (볼륨 포함)
docker compose down -v

# 특정 서비스만 재빌드
docker compose up -d --build [서비스명]

# 컨테이너 내부 접속
docker exec -it [컨테이너명] bash
```

### B. 환경 변수

| 변수 | 기본값 | 설명 |
|------|--------|------|
| DB_HOST | mariadb | 데이터베이스 호스트 |
| DB_PORT | 3306 | 데이터베이스 포트 |
| DB_SCHEMA | ldk | 데이터베이스 스키마 |
| DB_USERNAME | ldk | 데이터베이스 사용자 |
| DB_PASSWORD | 1q2w3e!Q@W#E | 데이터베이스 비밀번호 |
| SPRING_PROFILES_ACTIVE | docker / dev | Spring 프로필 |

### C. 포트 정리

| 포트 | 서비스 | 프로토콜 |
|------|--------|----------|
| 8080 | Jenkins Web UI | HTTP |
| 3310 | MariaDB | TCP |
| 7001 | Eureka Discovery | HTTP |
| 7002 | Auth Service | HTTP |
| 7003 | API Gateway | HTTP |

### D. 프로필 설명

| 프로필 | 용도 | Eureka 주소 |
|--------|------|-------------|
| local | 로컬 개발 (포트 17xxx) | http://localhost:17001/eureka |
| dev | 로컬 개발 (포트 7xxx) | http://localhost:7001/eureka |
| docker | Docker 컨테이너 | http://discovery:7001/eureka |
| prod | 운영 환경 | (설정 필요) |

---

## 변경 이력

| 버전 | 날짜 | 내용 |
|------|------|------|
| 1.0 | 2026-01-31 | 최초 작성 |
| 2.0 | 2026-02-01 | JDK 21로 변경, Jenkins 호스트 실행으로 변경, ARM32v7 최적화 |
| 2.1 | 2026-02-01 | Discovery unhealthy 트러블슈팅 추가 (actuator, logback-docker.xml) |
| 2.2 | 2026-02-01 | 서버 종료 및 재시작 가이드 추가 |
| 2.3 | 2026-02-01 | GitHub PAT 설정 가이드 추가, application-dev.yml 제거 |
| 3.0 | 2026-02-01 | Jenkins Docker 컨테이너화, 전체 5개 컨테이너 구성, 메모리 제한 설정 |
| 3.1 | 2026-02-01 | Jenkins WAR 로컬 다운로드 방식으로 변경 (오드로이드 빌드 멈춤 방지) |
| 3.2 | 2026-02-02 | Docker Compose V2 명령어로 변경 (`docker-compose` → `docker compose`) |
