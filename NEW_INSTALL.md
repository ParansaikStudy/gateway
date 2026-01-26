# ZQKSK Gateway 서버 설치 가이드 (처음부터)

> Ubuntu 24.04.3 LTS Server (x86_64) 에 아무것도 없는 상태에서 시작

## 서버 스펙

| 항목 | 스펙 |
|------|------|
| 모델 | Firebat T8 Plus |
| CPU | Intel N100 (4코어 / 4스레드, 3.4GHz) |
| RAM | 16GB |
| Storage | SSD 512GB |
| OS | Ubuntu 24.04.3 LTS Server (amd64) |

## 최종 구성도

```
┌──────────────────────────────────────────────────┐
│         Ubuntu 24.04.3 LTS Server (x86_64)       │
├──────────────────────────────────────────────────┤
│                                                   │
│  ┌─────────────── Docker ──────────────────────┐ │
│  │                                              │ │
│  │  MariaDB 11        :3310  [1GB]  (DB)       │ │
│  │  Discovery(Eureka) :7001  [1GB]  (서비스등록)│ │
│  │  Auth              :7002  [1GB]  (인증)      │ │
│  │  Gateway           :7003  [1GB]  (API라우팅) │ │
│  │  Jenkins           :8080  [1GB]  (CI/CD)    │ │
│  │                                              │ │
│  │  Docker Network: zqksk-network              │ │
│  └──────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────┘
```

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | JDK 25 |
| Spring Boot | 4.0.0 |
| Spring Cloud | 2025.1.0 |
| Gradle | 9.3.1 |
| MariaDB | 11 |
| Docker Base Image | ubuntu:24.04 |
| Sentry SDK | 8.31.0 |

---

## 1단계: Ubuntu 설치 직후 초기 설정

### 1.1 시스템 업데이트

```bash
sudo apt update && sudo apt upgrade -y
```

### 1.2 필수 패키지 설치

```bash
sudo apt install -y curl git net-tools vim ufw
```

### 1.3 타임존 설정

```bash
sudo timedatectl set-timezone Asia/Seoul

# 확인
date
```

### 1.4 서버 IP 확인

```bash
ip a
# 또는
hostname -I
```

> 이 IP를 기억해두세요. 이후 `서버IP`라고 적힌 곳에 이걸 넣으면 됩니다.

### 1.5 SSH 설정 (원격 접속용)

```bash
# 이미 설치되어 있을 수 있음
sudo apt install -y openssh-server
sudo systemctl enable ssh
sudo systemctl start ssh

# 상태 확인
sudo systemctl status ssh
```

> 이후 다른 PC에서 `ssh 사용자명@서버IP` 로 접속 가능

### 1.6 방화벽 설정

```bash
sudo ufw allow 22/tcp     # SSH
sudo ufw allow 8080/tcp   # Jenkins
sudo ufw allow 3310/tcp   # MariaDB
sudo ufw allow 7001/tcp   # Eureka Discovery
sudo ufw allow 7002/tcp   # Auth Service
sudo ufw allow 7003/tcp   # API Gateway
sudo ufw enable

# 확인
sudo ufw status
```

---

## 2단계: Docker 설치

### 2.1 Docker 설치에 필요한 패키지

```bash
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
```

### 2.2 Docker GPG 키 추가

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

### 2.3 Docker 저장소 추가

```bash
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu noble stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

> `noble` = Ubuntu 24.04 코드네임

### 2.4 Docker 설치

```bash
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

### 2.5 Docker 서비스 시작 + 자동시작

```bash
sudo systemctl start docker
sudo systemctl enable docker
```

### 2.6 현재 사용자에게 Docker 권한 부여

```bash
sudo usermod -aG docker $USER
newgrp docker
```

### 2.7 설치 확인

```bash
docker --version
# Docker version 28.x.x

docker compose version
# Docker Compose version v2.x.x

docker run hello-world
# Hello from Docker! 나오면 성공
```

---

## 3단계: JDK 25 설치

서버에서 직접 Gradle 빌드를 하기 위해 호스트에도 JDK가 필요합니다.

```bash
sudo apt install -y openjdk-25-jdk
```

> Ubuntu 24.04 기본 저장소에 JDK 25가 없을 경우:
> ```bash
> # AdoptOpenJDK 또는 수동 설치
> sudo apt install -y openjdk-21-jdk
> # 또는 SDKMAN 사용
> curl -s "https://get.sdkman.io" | bash
> source "$HOME/.sdkman/bin/sdkman-init.sh"
> sdk install java 25-open
> ```

### JAVA_HOME 설정

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64' >> ~/.bashrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

### 확인

```bash
java -version
echo $JAVA_HOME
```

---

## 4단계: 프로젝트 가져오기

### 4.1 Git SSH 키 생성 (GitHub 접속용)

```bash
ssh-keygen -t ed25519 -C "your-email@example.com"
# Enter 3번 누르기 (기본 경로, 비밀번호 없음)

cat ~/.ssh/id_ed25519.pub
```

> 출력된 키를 복사해서 GitHub > Settings > SSH and GPG keys > New SSH key에 등록

### 4.2 프로젝트 Clone

```bash
cd ~
git clone git@github.com:본인계정/gateway-dev.git
cd ~/gateway-dev
```

> SSH 안 쓰고 HTTPS로 할 경우:
> ```bash
> git clone https://github.com/본인계정/gateway-dev.git
> ```

### 4.3 프로젝트 구조 확인

```bash
ls -la ~/gateway-dev/
```

이런 구조여야 합니다:
```
~/gateway-dev/
├── docker-compose.yml
├── Jenkinsfile
├── gradlew
├── gradle.properties
├── settings.gradle.kts
├── docker/
│   ├── jenkins/Dockerfile
│   ├── discovery/Dockerfile
│   ├── auth/Dockerfile
│   ├── gateway/Dockerfile
│   └── mariadb/init/
├── api/
│   ├── discovery/
│   ├── auth/
│   └── gateway/
├── domain/
├── storage/
└── support/
```

---

## 5단계: 프로젝트 빌드

### 5.1 Gradle Wrapper 실행 권한 부여

```bash
cd ~/gateway-dev
chmod +x gradlew
```

### 5.2 전체 빌드

```bash
./gradlew clean build -x test
```

> 처음 빌드 시 Gradle 9.3.1 + 의존성 다운로드로 시간이 좀 걸립니다.
> 빌드 성공하면 `BUILD SUCCESSFUL` 출력됩니다.

### 5.3 빌드 결과 확인

```bash
ls -la api/discovery/build/libs/*.jar
ls -la api/auth/build/libs/*.jar
ls -la api/gateway/build/libs/*.jar
```

> 각각 JAR 파일이 있으면 빌드 성공

---

## 6단계: Jenkins WAR 다운로드

Jenkins 컨테이너 빌드에 필요합니다. (최초 1회)

```bash
cd ~/gateway-dev/docker/jenkins
curl -L -O https://get.jenkins.io/war-stable/latest/jenkins.war

# 다운로드 확인
ls -la jenkins.war
```

> 약 100MB 정도입니다.

---

## 7단계: Docker 컨테이너 실행

### 7.1 순차적 실행 (권장)

서비스 간 의존성이 있으므로 순서대로 띄워야 합니다.

```bash
cd ~/gateway-dev

# 1. MariaDB 먼저 (DB가 ready 되어야 함)
docker compose up -d mariadb

  restarting 계속 뜨면
  # 1. 먼저 컨테이너 중지
  docker compose down

  # 2. init 폴더 만들고 권한 주기
  mkdir -p ~/gateway-dev/docker/mariadb/init
  chmod 755 ~/gateway-dev/docker/mariadb/init

  # 3. 다시 실행
  docker compose up -d mariadb

  # 4. 상태 확인 (10초 정도 기다린 후)
  docker compose ps
  
  
# healthy 될 때까지 대기 (약 15~30초)
docker compose ps
# STATUS에 (healthy) 나올 때까지 기다리기

# 2. Discovery (Eureka - 서비스 등록소)
docker compose up -d discovery

# healthy 될 때까지 대기 (약 30~60초)
docker compose ps

# 3. Auth + Gateway (Discovery에 등록됨)
docker compose up -d auth gateway

# 4. Jenkins
docker compose up -d jenkins
```

### 7.2 한번에 실행할 경우

```bash
docker compose up -d --build
```

> `docker-compose.yml`에 `depends_on` + `healthcheck` 가 설정되어 있어서
> MariaDB healthy → Discovery healthy → Auth, Gateway 순서로 자동 실행됩니다.

### 7.3 상태 확인

```bash
docker compose ps
```

정상 출력:
```
NAME              IMAGE                   STATUS                    PORTS
zqksk-mariadb     mariadb:11              Up (healthy)              0.0.0.0:3310->3306/tcp
zqksk-discovery   gateway-dev-discovery   Up (healthy)              0.0.0.0:7001->7001/tcp
zqksk-auth        gateway-dev-auth        Up                        0.0.0.0:7002->7002/tcp
zqksk-gateway     gateway-dev-gateway     Up                        0.0.0.0:7003->7003/tcp
zqksk-jenkins     gateway-dev-jenkins     Up                        0.0.0.0:8080->8080/tcp
```

### 7.4 서비스 접속 확인

브라우저에서:

| 서비스 | URL | 정상 확인 |
|--------|-----|-----------|
| Eureka 대시보드 | http://서버IP:7001 | 웹페이지 보이면 OK |
| Jenkins | http://서버IP:8080 | 로그인 화면 보이면 OK |
| Auth 헬스체크 | http://서버IP:7002/actuator/health | `{"status":"UP"}` |
| Gateway 헬스체크 | http://서버IP:7003/actuator/health | `{"status":"UP"}` |

터미널에서:
```bash
curl http://localhost:7001/actuator/health
curl http://localhost:7002/actuator/health
curl http://localhost:7003/actuator/health
```

---

## 8단계: Jenkins 설정

### 8.1 초기 비밀번호 확인

```bash
docker exec zqksk-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

> 출력된 문자열을 복사

### 8.2 초기 설정

1. 브라우저에서 `http://서버IP:8080` 접속
2. 복사한 비밀번호 붙여넣기
3. **Install suggested plugins** 선택 (추천 플러그인 자동 설치)
4. 관리자 계정 생성 (아이디/비밀번호 설정)

### 8.3 필수 플러그인 설치

Jenkins 관리 > Plugins > Available plugins에서 검색 후 설치:
- **Docker Pipeline**
- **Pipeline**
- **Git**
- **GitHub Integration**

### 8.4 GitHub Personal Access Token (PAT) 만들기

Jenkins가 GitHub에서 코드를 가져오려면 PAT가 필요합니다.

1. GitHub 로그인
2. 오른쪽 상단 프로필 > **Settings**
3. 왼쪽 맨 아래 **Developer settings**
4. **Personal access tokens** > **Tokens (classic)**
5. **Generate new token (classic)**
6. Note: `jenkins-key`, Expiration: 원하는 기간
7. **repo** 체크박스 체크
8. **Generate token** 클릭
9. 나오는 `ghp_...` 문자열 복사 (한 번만 보여줌!)

### 8.5 Jenkins에 Credential 등록

1. Jenkins 관리 > **Credentials** > **System** > **Global credentials**
2. **Add Credentials**
3. 설정:
   - Kind: **Username with password**
   - Username: GitHub 사용자명
   - Password: `ghp_...` (위에서 복사한 토큰)
   - ID: `github-token`
4. **Create**

### 8.6 파이프라인 생성

1. Jenkins 메인 > **새로운 Item**
2. 이름 입력 (예: `gateway-dev`), **Pipeline** 선택 > OK
3. Pipeline 설정:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/본인계정/gateway-dev.git`
   - Credentials: `github-token` 선택
   - Branch: `*/main` (또는 사용하는 브랜치)
   - Script Path: `Jenkinsfile`
4. **저장**

### 8.7 첫 빌드 실행

파이프라인 페이지에서 **Build Now** 클릭

> Jenkinsfile에 Checkout > Gradle Build > Docker Build > Deploy > Health Check 순서로 실행됩니다.

---

## 9단계: 로그 확인

### 전체 로그

```bash
docker compose logs -f
```

### 서비스별 로그

```bash
docker compose logs -f mariadb
docker compose logs -f discovery
docker compose logs -f auth
docker compose logs -f gateway
docker compose logs -f jenkins
```

### 컨테이너 리소스 모니터링

```bash
docker stats
```

### 디스크 사용량

```bash
df -h
docker system df
```

---

## 10단계: 서버 종료 / 재시작

### 종료

```bash
cd ~/gateway-dev
docker compose stop
sudo shutdown -h now
```

### 재시작 후 확인

서버 켜지면 Docker가 자동 시작되고, `restart: unless-stopped` 설정으로 컨테이너도 자동으로 올라옵니다.

```bash
# Docker 확인
sudo systemctl status docker

# 컨테이너 확인
docker compose ps

# 헬스체크
curl http://localhost:7001/actuator/health
curl http://localhost:7002/actuator/health
curl http://localhost:7003/actuator/health
```

### 컨테이너가 안 올라온 경우

```bash
cd ~/gateway-dev
docker compose up -d
```

---

## 트러블슈팅

### MariaDB가 안 뜰 때

```bash
docker compose logs mariadb
# 권한 문제면:
docker compose down -v   # 주의: DB 데이터 삭제됨
docker compose up -d mariadb
```

### Discovery가 unhealthy 일 때

```bash
docker inspect zqksk-discovery --format='{{json .State.Health}}'
docker compose logs discovery
```

### Auth/Gateway가 안 뜰 때

보통 Discovery가 안 떠서 그럼. Discovery 먼저 확인.

```bash
docker compose logs auth
docker compose logs gateway
```

### 빌드 실패 시

```bash
# Gradle 캐시 삭제 후 재빌드
./gradlew clean build -x test --no-daemon

# Docker 캐시 삭제 후 재빌드
docker compose build --no-cache
docker compose up -d
```

### Docker 디스크 정리

```bash
# 안 쓰는 이미지/컨테이너 삭제
docker system prune -f

# 전부 삭제 (주의)
docker system prune -a
```

### Jenkins에서 Docker 명령어 실패

```bash
# Docker 소켓 권한
sudo chmod 666 /var/run/docker.sock

# 확인
docker exec zqksk-jenkins docker ps
```

### Gradle 9 빈 모듈 에러

```
Configuring project ':api:batch-api' without an existing directory is not allowed.
```

→ `settings.gradle.kts`에서 존재하지 않는 모듈의 `include()` 제거

### Spring Boot 4.0 logback 에러

```
Could not resolve placeholder 'spring.profiles.active'
```

→ `logging.yml`에서 `logging.config: classpath:logback/logback-${spring.profiles.active}.xml` 제거하고 `logback-spring.xml`에서 `<springProfile>` 태그로 처리

---

## 유용한 명령어 모음

```bash
# 전체 시작
docker compose up -d

# 전체 중지
docker compose stop

# 전체 삭제 (볼륨 유지)
docker compose down

# 전체 삭제 (DB 데이터도 삭제)
docker compose down -v

# 특정 서비스만 재빌드
docker compose up -d --build auth

# 컨테이너 내부 접속
docker exec -it zqksk-auth bash

# MariaDB 접속
docker exec -it zqksk-mariadb mysql -u ldk -p
# 비밀번호: 1q2w3e!Q@W#E

# 리소스 모니터링
docker stats

# Gradle 빌드
./gradlew clean build -x test

# 개별 빌드
./gradlew :api:discovery:bootJar -x test
./gradlew :api:auth:bootJar -x test
./gradlew :api:gateway:bootJar -x test
```

## 포트 정리

| 포트 | 서비스 | 용도 |
|------|--------|------|
| 22 | SSH | 원격 접속 |
| 3310 | MariaDB | 데이터베이스 |
| 7001 | Discovery (Eureka) | 서비스 등록/발견 |
| 7002 | Auth | 인증 서비스 |
| 7003 | Gateway | API 라우팅 |
| 8080 | Jenkins | CI/CD |

## 환경 변수 (docker-compose.yml에 설정됨)

| 변수 | 값 | 설명 |
|------|-----|------|
| SPRING_PROFILES_ACTIVE | docker | Spring 프로필 |
| DB_HOST | mariadb | DB 호스트 (Docker 내부) |
| DB_PORT | 3306 | DB 포트 (Docker 내부) |
| DB_SCHEMA | ldk | DB 스키마 |
| DB_USERNAME | ldk | DB 사용자 |
| DB_PASSWORD | 1q2w3e!Q@W#E | DB 비밀번호 |
| MARIADB_ROOT_PASSWORD | root1234! | MariaDB root 비밀번호 |
