plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.androidMapLibrary)
    id("kotlin-android")
    kotlin("kapt")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.8.10"
}

// kapt is fucked up switch to ksp, this is a workaround
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KaptGenerateStubs::class.java).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

android {
    namespace = "com.example.datatrap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.datatrap"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // also for the min sdk
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// group:name:version
dependencies {
    // core
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // or Material Design 2
    implementation(libs.androidx.compose.material)
    // Android Studio Preview support
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // UI Tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Optional - Add full set of material icons
    implementation(libs.androidx.compose.material.icons.extended)
    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // navigation
    implementation(libs.androidx.navigation.compose)
    // lifecycle aware state
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.room.ktx)

    // fused location provider
    implementation(libs.play.services.location)
    // map 17.0.1
    implementation(libs.play.services.maps)
    // compose map
    implementation(libs.maps.compose)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    // gson
    implementation(libs.converter.gson)
    // scalars
    implementation(libs.converter.scalars)

    // kotlin datetime picker
    implementation (libs.datetime)
    // needed because min sdk is lower than 26 - to use java LocalDateTime
    coreLibraryDesugaring (libs.desugar.jdk.libs)

    // coil
    implementation(libs.coil.compose)

    // kotlin serial
    implementation(libs.kotlinx.serialization.json)

    //koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)
}