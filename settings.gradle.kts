rootProject.name = "BareBones"

include("api")
include("internal")

pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    versionCatalogs {

        create("libs") {
            val jetbrainsAnnotations: String by settings
            library("jetbrains-annotations", "org.jetbrains:annotations:$jetbrainsAnnotations")

            val junit: String by settings
            library("junit-api", "org.junit.jupiter:junit-jupiter-api:$junit")
            library("junit-engine", "org.junit.jupiter:junit-jupiter-engine:$junit")
            library("junit-params", "org.junit.jupiter:junit-jupiter-params:$junit")

            val lombok: String by settings
            library("lombok", "org.projectlombok:lombok:$lombok")

            val asm: String by settings
            library("asm", "org.ow2.asm:asm:$asm")
            library("asm-commons", "org.ow2.asm:asm-commons:$asm")

            val netty: String by settings
            library("netty", "io.netty:netty-all:$netty")

            val googleGuava: String by settings
            library("google-guava", "com.google.guava:guava:$googleGuava")

            val googleAutoservice: String by settings
            library("google-autoservice", "com.google.auto.service:auto-service:$googleAutoservice")

            val googleGson: String by settings
            library("google-gson", "com.google.code.gson:gson:$googleGson")

            val fastUtil: String by settings
            library("fastutil", "it.unimi.dsi:fastutil:$fastUtil")

            val paklet: String by settings
            library("paklet-api", "org.machinemc:paklet-api:$paklet")
            library("paklet-core", "org.machinemc:paklet-core:$paklet")
            library("paklet-processor", "org.machinemc:paklet-processor:$paklet")

            plugin("paklet-plugin", "paklet-plugin").version("1.0.0")
        }


    }
}
