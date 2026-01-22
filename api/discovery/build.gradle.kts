plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation(project(":support:logging"))

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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