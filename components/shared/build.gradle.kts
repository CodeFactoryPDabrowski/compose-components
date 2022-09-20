plugins {
    id("android.library")
    id("android.library.compose")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.uiUtil)
    api(libs.androidx.compose.uiToolingPreview)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.uiTooling)
}
