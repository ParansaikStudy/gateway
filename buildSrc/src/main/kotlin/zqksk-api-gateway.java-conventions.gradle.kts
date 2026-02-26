plugins {
    id("java")
    id("java-library")
    id("jacoco")
}

allprojects {
    group = "com.zqksk"
    version = "${property("appVersion")}"
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
}

repositories {
    mavenCentral()
}

tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

with(extensions.getByType(JacocoPluginExtension::class.java)) {
    toolVersion = "0.8.11"
}