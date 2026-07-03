plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.edge.integrated"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.edge.integrated"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-alpha1"

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/DEPENDENCIES",
                "META-INF/INDEX.LIST"
            )
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }

    splits {
        abi {
            isEnable = false
            isUniversalApk = true
        }
    }
}

dependencies {
    // Termux modules
    implementation(project(":termux-module:terminal-emulator"))
    implementation(project(":termux-module:terminal-view"))
    implementation(project(":termux-module:termux-shared"))

    // Winlator core
    implementation(project(":winlator-core"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.manager)
    implementation(libs.androidx.security.crypto)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.foundation)
    implementation(libs.compose.animation)
    implementation(libs.compose.constraintlayout)
    debugImplementation(libs.compose.ui.tooling)

    // Hilt DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Network
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin)

    // Image Loading
    implementation(libs.coil.compose)

    // Google AI Edge / MediaPipe / LiteRT
    implementation(libs.mediapipe.tasks.vision)
    implementation(libs.mediapipe.tasks.text)
    implementation(libs.mediapipe.tasks.audio)
    implementation(libs.litert.lm)
    implementation(libs.litert.runtime)
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.huggingface.hub)

    // JSON
    implementation(libs.json)

    // gRPC
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.grpc.stub)
}
