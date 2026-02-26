plugins {
    java
    application
}

group = "com.zqksk"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
}

application {
    mainClass.set("com.zqksk.stock.StockAnalyzerApp")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}
