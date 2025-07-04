import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services.plugin)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.navigation)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.hiltAndroid)
}

android {
    namespace = "kr.co.rolling.moment"
    compileSdk = 35

    var baseVersionCode = 24063000  // Define your base version code

    defaultConfig {
        applicationId = "kr.co.rolling.moment"
        minSdk = 26
        targetSdk = 35
        versionCode = baseVersionCode
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("releaseSignConfig") {
            keyAlias = getApiKey("SIGN_KEY_ALIAS")
            keyPassword = getApiKey("KEY_PASSWORD")
            storeFile = rootProject.file("rollin_keystore")
            storePassword = getApiKey("KEY_PASSWORD")
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("releaseSignConfig")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField("String", "KAKAO_NATIVE_KEY", getApiKey("KAKAO_API_KEY"))
            buildConfigField("String", "NAVER_CLIENT_KEY", getApiKey("NAVER_CLIENT_KEY"))
            buildConfigField("String", "NAVER_CLIENT_ID", getApiKey("NAVER_CLIENT_ID"))
            buildConfigField("String", "AES_KEY", getApiKey("AES_KEY"))
            buildConfigField("String", "AES_VECTOR_KEY", getApiKey("AES_VECTOR_KEY"))
        }
        debug {
            isDebuggable = true

            buildConfigField("String", "KAKAO_NATIVE_KEY", getApiKey("KAKAO_API_KEY"))
            buildConfigField("String", "NAVER_CLIENT_KEY", getApiKey("NAVER_CLIENT_KEY"))
            buildConfigField("String", "NAVER_CLIENT_ID", getApiKey("NAVER_CLIENT_ID"))
            buildConfigField("String", "AES_KEY", getApiKey("AES_KEY"))
            buildConfigField("String", "AES_VECTOR_KEY", getApiKey("AES_VECTOR_KEY"))
        }
    }

    flavorDimensions.add("version")

    productFlavors {
        create("dev") {
            versionCode = baseVersionCode
            buildConfigField("kr.co.rolling.moment.library.data.Deploy.ServerType", "SERVER_TYPE", "kr.co.rolling.moment.library.data.Deploy.ServerType.DEVELOP")
            buildConfigField("boolean", "ADMIN", "false")
        }
        create("prod") {
            versionCode = baseVersionCode + 20
            buildConfigField("kr.co.rolling.moment.library.data.Deploy.ServerType", "SERVER_TYPE", "kr.co.rolling.moment.library.data.Deploy.ServerType.RELEASE")
            buildConfigField("boolean", "ADMIN", "false")
        }
        create("admin") {
            versionCode = baseVersionCode + 40
            buildConfigField("kr.co.rolling.moment.library.data.Deploy.ServerType", "SERVER_TYPE", "kr.co.rolling.moment.library.data.Deploy.ServerType.DEVELOP")
            buildConfigField("boolean", "ADMIN", "true")
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
        viewBinding = true
        buildConfig = true
    }
}

fun getApiKey(propertyKey: String): String {
    val properties = Properties().apply {
        load(FileInputStream(rootProject.file("local.properties")))
    }
    return properties.getProperty(propertyKey) ?: ""
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.crashlytics.buildtools)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Google Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.messaging)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // RxJava3
    implementation(libs.rxandroid)
    implementation(libs.rxjava)

    // RxJava3 + Retrofit2
    implementation(libs.rxjava3.retrofit.adapter)

    // Retrofit2
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.common)

    // Sandwich
    implementation(libs.sandwich)

    // Espresso
    implementation(libs.androidx.espresso.idling.resource)

    // Retrofit2 + OkHttp3
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)

    // Camera
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.dagger.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    kspTest(libs.hilt.android.compiler)

    // Timber (log)
    implementation(libs.timber)

    // 카카오 로그인 API 모듈
    implementation(libs.v2.user)

    // 카카오 공유하기 API
    implementation(libs.v2.share)

    // Naver 로그인 API 모듈
    implementation(libs.oauth)

    // glide
    implementation(libs.glide)
    ksp(libs.glide.compier)

    // Recycler View
    implementation(libs.androidx.recyclerview)
    implementation(libs.flexbox)
}