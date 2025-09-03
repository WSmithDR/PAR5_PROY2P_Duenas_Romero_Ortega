plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.par5_proy2p_duenas_romero_ortega"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.par5_proy2p_duenas_romero_ortega"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\PC\\AppData\\Local\\Android\\Sdk\\platforms\\android-36",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf<String>()
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}