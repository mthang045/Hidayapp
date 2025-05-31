buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.google.gms:google-services:4.3.15")  // Plugin Google Services
    }
}

plugins {
    // Các plugin apply false nếu bạn dùng trong module riêng
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
