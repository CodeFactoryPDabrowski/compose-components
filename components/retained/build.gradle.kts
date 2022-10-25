plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.przeman.retained"
}

dependencies {
    implementation(project(":components:shared"))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
}
