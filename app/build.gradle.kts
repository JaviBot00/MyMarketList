plugins {
    // Android
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Firebase
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.politecnicomalaga.mymarketlist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.politecnicomalaga.mymarketlist"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
//    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // My implementations
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    implementation("com.jjoe64:graphview:4.2.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    //    implementation 'com.github.Redman1037:TSnackBar:V2.0.0'
    //    implementation 'com.google.code.gson:gson:2.8.9'

    // Android implementations
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}