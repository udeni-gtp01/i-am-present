// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
//        classpath ("com.android.tools.build:gradle:8.1")
//        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:8.0.2")
//        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}