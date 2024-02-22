plugins {
    id("java-library-convention")
}

repositories {
    maven {
        url = uri("http://www.machinemc.org/releases")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.google.guava)
    implementation(libs.google.gson)
    implementation(libs.netty)
    implementation(libs.fastutil)
}