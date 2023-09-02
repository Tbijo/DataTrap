plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    // kotlin annotation processing
    id("kotlin-kapt")
    // na mapu
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    // na dagger hilt
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.datatrap"
    compileSdk = 33

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

dependencies {
    // core
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // compose
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    // or Material Design 2
    implementation("androidx.compose.material:material")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.7.2")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")
    // lifecycle aware state
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    //Room dependencies
    val room_version = "2.4.2"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    // fused location provider
    implementation("com.google.android.gms:play-services-location:19.0.1")
    // map 17.0.1
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    // compose map
    implementation("com.google.maps.android:maps-compose:2.14.0")

    // na dagger hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-compiler:2.42")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // scalars
    implementation("com.squareup.retrofit2:converter-scalars:2.1.0")

    // kotlin datetime picker
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    // needed because min sdk is lower than 26 - to use java LocalDateTime
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.6")

}