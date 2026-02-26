// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android
    alias(libs.plugins.android.application) apply false

    // Firebase
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
//    id("com.google.gms.google-services") version "4.4.4" apply false
//    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}