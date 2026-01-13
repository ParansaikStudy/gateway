plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("zqksk-api-gateway.java-conventions")
}


dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework.data:spring-data-commons")
    implementation("com.fasterxml.uuid:java-uuid-generator:5.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}