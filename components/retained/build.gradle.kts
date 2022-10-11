plugins {
    id("android.library")
    id("android.library.compose")
}

dependencies {
    implementation(project(":components:shared"))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
}
