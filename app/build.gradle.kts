plugins {
    // Android
    alias(libs.plugins.android.application)

    // Firebase
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.hotguy.mymarketlist"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
//    buildToolsVersion = "36.1.0"

    defaultConfig {
        applicationId = "com.hotguy.mymarketlist"
        minSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
//    implementation("com.google.firebase:firebase-crashlytics")
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)

    // My implementations
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.okhttp)
    implementation(libs.roundedimageview)
    implementation(libs.androidx.swiperefreshlayout)
//    implementation("com.jjoe64:graphview:4.2.2")
    implementation(libs.picasso)
    //    implementation 'com.github.Redman1037:TSnackBar:V2.0.0'
    //    implementation 'com.google.code.gson:gson:2.8.9'
//    implementation("com.android.tools.build:gradle:8.5.2")

    // Android implementations
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

//    implementation("androidx.activity:activity-ktx:1.10.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}