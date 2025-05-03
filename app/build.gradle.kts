plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.sricedemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sricedemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.viewpager2)
    implementation(libs.material)
    // Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose Libraries
    implementation(platform(libs.androidx.compose.bom))  // BOM to manage all Compose versions
    implementation(libs.androidx.ui)                    // androidx.compose.ui
    implementation(libs.androidx.ui.graphics)           // androidx.compose.ui-graphics
    implementation(libs.androidx.ui.tooling.preview)    // androidx.compose.ui-tooling-preview
    implementation(libs.exoplayer)
    // Material3
    implementation(libs.androidx.material3)             // androidx.compose.material3

    // AppCompat
    implementation(libs.androidx.appcompat)             // androidx.appcompat

    // Testing Dependencies
    testImplementation(libs.junit)                     // junit
    androidTestImplementation(libs.androidx.junit)      // androidx.test.ext.junit
    androidTestImplementation(libs.androidx.espresso.core) // androidx.test.espresso.espresso-core
    androidTestImplementation(libs.androidx.ui.test.junit4)  // androidx.compose.ui.test.junit4
    implementation(libs.androidx.appcompat)

    // Debugging and UI Testing
    debugImplementation(libs.androidx.ui.tooling)          // androidx.compose.ui.tooling
    debugImplementation(libs.androidx.ui.test.manifest)    // androidx.compose.ui.test.manifest
}
