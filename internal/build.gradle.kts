import org.machinemc.paklet.plugin.PakletPlugin

buildscript {
    repositories {
        maven {
            url = uri("https://repo.machinemc.org/releases")
        }
    }
    dependencies {
        val paklet = libs.plugins.paklet.plugin.get()
        classpath("org.machinemc:${paklet.pluginId}:${paklet.version}")
    }
}

plugins {
    id("java-library-convention")
}

apply<PakletPlugin>()

repositories {
    maven {
        url = uri("https://repo.machinemc.org/releases")
    }
}

dependencies {
    implementation(project(":api"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.asm)
    implementation(libs.google.guava)
    implementation(libs.google.gson)
    implementation(libs.netty)

    implementation(libs.paklet.api)
    implementation(libs.paklet.core)
    annotationProcessor(libs.paklet.processor)
}
