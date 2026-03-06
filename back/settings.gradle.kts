rootProject.name = "zqksk-api-gateway"

include("api:auth")
include("api:discovery")
include("api:gateway")
include("api:stock")
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
    plugins {
        id("org.springframework.boot") version springBootVersion
        id("zqksk-api-gateway.java-conventions")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}