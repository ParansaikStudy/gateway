# ============================================
# Jenkins CI/CD Server (x86_64)
# ============================================
# 주의: jenkins.war는 미리 다운로드 후 서버로 전송해야 합니다.
# curl -O https://get.jenkins.io/war-stable/latest/jenkins.war
# ============================================
FROM ubuntu:24.04

WORKDIR /var/jenkins_home

# 타임존 미리 설정 (interactive 프롬프트 방지)
ENV TZ=Asia/Seoul
ENV DEBIAN_FRONTEND=noninteractive

# 필수 패키지 및 JDK 25 설치
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && apt-get update && apt-get install -y \
    openjdk-25-jdk-headless \
    curl \
    git \
    tzdata \
    docker.io \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Docker Compose V2 수동 설치 (x86_64용)
RUN mkdir -p /usr/local/lib/docker/cli-plugins \
    && curl -SL "https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64" \
       -o /usr/local/lib/docker/cli-plugins/docker-compose \
    && chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

ENV JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV JENKINS_HOME=/var/jenkins_home

# 미리 다운로드된 Jenkins WAR 복사
COPY docker/jenkins/jenkins.war /opt/jenkins.war

EXPOSE 8080

# Jenkins 실행 (메모리 제한 설정)
ENTRYPOINT ["java", "-Xmx768m", "-Xms256m", "-jar", "/opt/jenkins.war", "--httpPort=8080"]
