plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation(project(":support:core-exception"))
    implementation(project(":support:core-web"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
