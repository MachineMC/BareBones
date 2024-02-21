plugins {
    id("java-library-convention")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.google.guava)
    implementation(libs.fastutil)
}