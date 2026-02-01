import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.gradle)
}

android {
    namespace = "com.spundev.ringvolume"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.spundev.ringvolume"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val storePath = System.getenv("SIGNING_STORE_PATH")
            storePath?.let {
                storeFile = file(storePath)
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            // Switch to debug signingConfigs to create release builds during development
            // signingConfig = signingConfigs.named("debug").get()
        }
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel2api32") {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // Use only API levels 27 and higher.
                    // ATDs should support only API level 30, but this is working fine ¿?
                    apiLevel = 32
                    // To include Google services, use "google-atd".
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Activity
    implementation(libs.androidx.activity.compose)
    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    // Compose tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // After updating to Kotlin 2.3.0, we got this error when running the app:
    //     [Hilt] Provided Metadata instance has version 2.3.0, while maximum supported version is 2.2.0.
    // Reports in the Dagger repository were closed saying that this was not something they should fix.
    // To fix it ourselves, we need to add the dependency with the correct version manually.
    // NOTE: We are not moving this to libs.versions.toml to make it clear that this is a temp fix and
    //  that we should try to remove this line in the future to check if they changed their mind.
    ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.versions.kotlin.get()}")
    // Hilt + AndroidX
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    // Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    // Serialization
    implementation(libs.kotlinx.serialization.json)
    // Work
    implementation(libs.androidx.work.runtime.ktx)

    // TESTS
    // For instrumentation tests
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.test)
    // For local unit tests
    testImplementation(libs.androidx.test.ext)
}