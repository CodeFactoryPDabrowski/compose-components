plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.przeman.arch"
}


dependencies {
    implementation(libs.androidx.compose.foundation)
}
