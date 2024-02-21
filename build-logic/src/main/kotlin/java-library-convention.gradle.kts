import org.machinemc.barebones.CheckStyleProvider

plugins {
    java
    `java-library`
    checkstyle
}

val group: String by project
setGroup(group)

val version: String by project
setVersion(version)

val libs = project.rootProject
    .extensions
    .getByType(VersionCatalogsExtension::class)
    .named("libs")

//
// Repositories and Dependencies
//

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.findLibrary("jetbrains-annotations").get())

    testImplementation(libs.findLibrary("junit-api").get())
    testRuntimeOnly(libs.findLibrary("junit-engine").get())
    testImplementation(libs.findLibrary("junit-params").get())
}

//
// Java configuration
//

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

//
// Checkstyle configuration
//

checkstyle {
    toolVersion = "10.13.0" // required for String templates
    config = resources.text.fromUri(CheckStyleProvider.get())
}

dependencies {
    modules {
        // Replace old dependency `google-collections` with `guava`
        // This is required for checkstyle to work
        module("com.google.collections:google-collections") {
            replacedBy("com.google.guava:guava", "google-collections is part of guava")
        }
    }
}

//
// Preview features
//

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
}
tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}

//
// Task configurations
//

tasks {
    compileJava {
        options.release.set(21)
        options.encoding = Charsets.UTF_8.name()
        // Can be used for debugging
        // options.compilerArgs.addAll(listOf("-Xlint:preview", "-Xlint:unchecked", "-Xlint:deprecation"))
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    test {
        useJUnitPlatform()
    }
}