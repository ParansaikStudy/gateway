rootProject.name = "zqksk-api-gateway"

include("api:external-api")
include("api:internal-api")
include("api:batch-api")
include("api:push-api")
include("api:auth")
include("api:discovery")
include("api:gateway")
include("domain:customer")
include("domain:dentistry")
include("domain:user")
include("domain:log")
include("domain:pc")
include("domain:notices")
include("domain:competitor")
include("domain:common")
include("storage:database")
include("support:core-exception")
include("support:core-web")
include("support:logging")
include("support:mail")

pluginManagement {
    val springBootVersion : String by settings
    val springDependencyManagementVersion : String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("zqksk-api-gateway.java-conventions")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}