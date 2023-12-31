plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // ADD para poder pasar objetos entre actividades tienen que ser parcelables
    // haremos las clases con los datos parcelables
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.grupo2.speakr"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.grupo2.speakr"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        // ADD para que automaticamente asocie clases a vistas xml "MainActivityBinding" etc
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.4.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // ADD retrofit + gson para la conversion de strings en json a objetos y viceversa
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // ADD para utilizar viewmodels
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
}