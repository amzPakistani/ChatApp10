plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.9.23"
    kotlin("kapt")

    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

android {
    namespace = "com.example.chatapp10"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatapp10"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback.classic)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.json)

    implementation (libs.hilt.android)
    implementation (libs.androidx.hilt.lifecycle.viewmodel)
    implementation (libs.androidx.hilt.navigation.compose)

    ksp("com.google.dagger:hilt-compiler:2.38.1")


}

kotlin {
    sourceSets["main"].kotlin.srcDir("build/generated/ksp/main/kotlin")
}
//implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
//implementation("androidx.navigation:navigation-compose:2.7.7")
//
//implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//
//implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
//
//implementation("io.ktor:ktor-client-core:2.3.12")
//implementation("io.ktor:ktor-client-cio:2.3.12")
//implementation("io.ktor:ktor-client-serialization:2.3.12")
//implementation("io.ktor:ktor-client-websockets:2.3.12")
//implementation("io.ktor:ktor-client-logging:2.3.12")
//implementation("ch.qos.logback:logback-classic:1.4.14")
//
//implementation("io.ktor:ktor-client-content-negotiation:2.0.0")
//implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
//
//implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
//implementation("io.ktor:ktor-client-json:2.3.12")
//
//implementation ("com.google.dagger:hilt-android:2.49")
//implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
//implementation ("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")