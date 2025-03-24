plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services.plugin)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.navigation)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "kr.co.rolling.moment"
    compileSdk = 35

    defaultConfig {
        applicationId = "kr.co.rolling.moment"
        minSdk = 26
        targetSdk = 35
        versionCode = 24031700
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Google Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)

    // Navigation
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    // RxJava3
    implementation (libs.rxandroid)
    implementation (libs.rxjava)

    // RxJava3 + Retrofit2
    implementation (libs.rxjava3.retrofit.adapter)

    // Retrofit2
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.converter.moshi)
    implementation (libs.gson)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.core.common)

    // Sandwich
    implementation (libs.sandwich)

    // Espresso
    implementation (libs.androidx.espresso.idling.resource)

    // Retrofit2 + OkHttp3
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // kotlin serialization
    implementation (libs.kotlinx.serialization.json)

    // EncryptedSharedPreferences
    implementation (libs.androidx.security.crypto)

    // Camera
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.lifecycle)

    // Hilt
    implementation(libs.hilt.android)
    ksp (libs.hilt.android.compiler)
    androidTestImplementation (libs.dagger.hilt.android.testing)
    testImplementation (libs.dagger.hilt.android.testing)
    kspAndroidTest (libs.hilt.android.compiler)
    kspTest (libs.hilt.android.compiler)

    // Timber (log)
    implementation (libs.timber)
}