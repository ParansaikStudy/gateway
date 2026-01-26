plugins {
    id("org.springframework.boot")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}"))

    implementation(project(":domain:user"))
    implementation(project(":storage:database"))
    implementation(project(":support:core-exception"))
    implementation(project(":support:core-web"))
    implementation(project(":support:logging"))
    implementation(project(":support:mail"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("com.fasterxml.uuid:java-uuid-generator:5.0.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}