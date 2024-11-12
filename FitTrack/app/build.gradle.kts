import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.fittrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fittrack"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load the values from .properties file
        val keystoreFileExercise = project.rootProject.file("local.properties")
        val propertiesExercise = Properties()
        propertiesExercise.load(keystoreFileExercise.inputStream())

        // Return empty key in case something goes wrong
        val apiKeyExercise = propertiesExercise.getProperty("API_KEY_EXERCISE") ?: ""

        buildConfigField(
            type = "String",
            name = "API_KEY_EXERCISE",
            value = apiKeyExercise
        )

    }

    buildFeatures {
        buildConfig = true // Enable custom BuildConfig fields
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation (libs.jackson.databind)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}