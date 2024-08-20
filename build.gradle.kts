// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // gradle
    alias(libs.plugins.androidApplication) apply false
    // kotlin
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false

    alias(libs.plugins.androidMapLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.pluginSerialization) apply false
}