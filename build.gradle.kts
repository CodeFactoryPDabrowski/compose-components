plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
