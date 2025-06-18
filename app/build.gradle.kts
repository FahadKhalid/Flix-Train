plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.googleDaggerHiltAndroid)
}

android {
    namespace = "com.app.flixtrain"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.flixtrain"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "https://gist.githubusercontent.com/mootazltaief/6f6ae202071449386b57eb3876ce25ee/raw/")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Kotlin Coroutines for asynchronous operations
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Retrofit for network calls
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson) // For JSON parsing

    // OkHttp for network logging (useful for debugging)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Room for local database (Offline-First)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Kotlin extensions for Room
    ksp(libs.androidx.room.compiler) // KSP for Room annotation processing

    // Dagger Hilt for Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // KSP for Hilt annotation processing
    implementation(libs.androidx.hilt.navigation.compose) // Hilt integration for Compose Navigation

    // Jetpack Compose Navigation
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}