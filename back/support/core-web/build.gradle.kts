plugins {
    id("org.springframework.boot")
    id("zqksk-api-gateway.java-conventions")
}

dependencies {
    implementation(project(":support:core-exception"))
    implementation("org.slf4j:jul-to-slf4j")
    implementation("org.springframework:spring-webflux")
    implementation("org.springframework:spring-web")
}