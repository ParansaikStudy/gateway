plugins {
    id("org.springframework.boot")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation(project(":domain:customer"))
    implementation(project(":domain:dentistry"))
    implementation(project(":domain:user"))
    implementation(project(":domain:log"))
    implementation(project(":domain:pc"))
    implementation(project(":domain:competitor"))
    implementation(project(":domain:common"))
    implementation(project(":domain:notices"))
    implementation(project(":support:core-exception"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}