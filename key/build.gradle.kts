plugins {
    id("java-library-convention")
    `maven-publish`
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.google.guava)
}

publishing {
    repositories {
        maven {
            name = "machine"
            url = uri("https://repo.machinemc.org/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.machinemc"
            artifactId = "barebones-key"
            version = project.version.toString()
            from(components["java"])
        }
    }
}
