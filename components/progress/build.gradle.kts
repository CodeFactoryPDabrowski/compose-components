plugins {
    id("android.library")
    id("android.library.compose")
}

dependencies {
    implementation(project(":components:shared"))
    debugImplementation(libs.bundles.androidx.compose.previewWorkaround)
}
