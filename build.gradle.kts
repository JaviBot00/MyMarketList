//buildscript {
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.5.2")
//        classpath("com.google.gms:google-services:4.4.2")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
//    }
//}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android
    id("com.android.application") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // Firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
}