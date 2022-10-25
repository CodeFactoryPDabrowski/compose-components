plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.przeman.progress"
}

dependencies {
    implementation(project(":components:shared"))
    debugImplementation(libs.bundles.androidx.compose.previewWorkaround)
}
