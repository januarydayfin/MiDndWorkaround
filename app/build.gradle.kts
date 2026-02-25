import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.krayapp.dndworkaround"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.krayapp.dndfixer"
        minSdk = 29
        targetSdk = 36
        versionCode = 106
        versionName = "1.06"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.review.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.tedpermission)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}