plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.androidMapLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.pluginSerialization)
    id("kotlin-android")
    id("kotlin-parcelize")
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
        kotlinCompilerExtensionVersion = "1.5.0"
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
    // Material 3
    implementation(libs.androidx.compose.material3)
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
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.ktx)

    // fused location provider
    implementation(libs.play.services.location)
    // map 17.0.1
    implementation(libs.play.services.maps)
    // compose map
    implementation(libs.maps.compose)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    // kotlin serial
    implementation(libs.kotlinx.serialization.json)

    // kotlin datetime picker
    implementation (libs.datetime)
    // needed because min sdk is lower than 26 - to use java LocalDateTime
    coreLibraryDesugaring (libs.desugar.jdk.libs)

    // coil
    implementation(libs.coil.compose)

    //koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)
}