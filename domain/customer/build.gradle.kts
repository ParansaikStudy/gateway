plugins {
    id("org.springframework.boot")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("com.fasterxml.uuid:java-uuid-generator:5.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}