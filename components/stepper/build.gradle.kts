plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.przeman.stepper"
}

dependencies {
    implementation(project(":components:shared"))
    debugImplementation(libs.bundles.androidx.compose.previewWorkaround)
}
