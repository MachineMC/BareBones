import org.machinemc.paklet.plugin.PakletPlugin

plugins {
    id("java-library-convention")
}

buildscript {
    repositories {
        maven {
            url = uri("http://www.machinemc.org/releases")
            isAllowInsecureProtocol = true
        }
    }
    dependencies {
        val paklet = libs.plugins.paklet.plugin.get();
        classpath("org.machinemc:${paklet.pluginId}:${paklet.version}")
    }
}

apply<PakletPlugin>()

repositories {
    maven {
        url = uri("http://www.machinemc.org/releases")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation(project(":api"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.asm)
    implementation(libs.google.gson)
    implementation(libs.netty)

    implementation(libs.paklet.api)
    implementation(libs.paklet.core)
    annotationProcessor(libs.paklet.processor)
}
