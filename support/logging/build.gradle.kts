plugins {
    id("org.springframework.boot")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-tracing-bridge-brave") {
        exclude(group = "io.zipkin.reporter2")
    }
    implementation("io.sentry:sentry-logback:${property("sentryVersion")}")
}