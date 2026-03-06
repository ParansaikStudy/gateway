# ============================================
# Eureka Discovery Service (x86_64)
# ============================================
# ./gradlew :api:discovery:bootJar -x test
# ============================================
FROM ubuntu:24.04

WORKDIR /app

ENV TZ=Asia/Seoul
ENV DEBIAN_FRONTEND=noninteractive

# JRE 25 설치
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && apt-get update && apt-get install -y openjdk-25-jre-headless curl tzdata \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# 빌드된 JAR 복사
COPY api/discovery/build/libs/*.jar app.jar

EXPOSE 7001

ENTRYPOINT ["java", "-jar", "app.jar"]
