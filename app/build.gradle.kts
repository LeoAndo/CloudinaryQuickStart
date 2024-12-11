import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "jp.ac.jec.cm0199.cloudinaryquickstart"
    compileSdk = 35

    // TODO WORKAROUND: START
    configurations.all {
        resolutionStrategy {
            force("androidx.emoji2:emoji2:1.2.0") // Replace with the desired version
        }
    }
    // TODO WORKAROUND: END

    defaultConfig {
        applicationId = "jp.ac.jec.cm0199.cloudinaryquickstart"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { stream ->
                localProperties.load(stream)
            }
        }
        val cloudName:String = localProperties.getProperty("CLOUD_NAME")
        val uploadPreset:String = localProperties.getProperty("UPLOAD_PRESET")
        buildConfigField("String", "CLOUD_NAME", cloudName)
        buildConfigField("String", "UPLOAD_PRESET", uploadPreset)
    }

    buildFeatures {
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.cloudinary:cloudinary-android:3.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}