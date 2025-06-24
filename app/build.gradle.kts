plugins {
    id("com.android.application") version "8.10.1" apply true
    id("com.google.gms.google-services") version "4.4.2" apply true
}

android {
    compileSdk = 33

    namespace = "com.hidaymovie"  // Thay đổi namespace để khớp với package name trong google-services.json

    defaultConfig {
        applicationId = "com.hidaymovie"  // Thay đổi applicationId để khớp với package name trong google-services.json
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // Hoặc JavaVersion.VERSION_11 nếu bạn muốn sử dụng Java 11
        targetCompatibility = JavaVersion.VERSION_17  // Hoặc JavaVersion.VERSION_11
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-firestore:24.0.0")
    implementation("com.google.firebase:firebase-storage:20.0.1") // Firebase Storage

    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")

    // Material Design
    implementation("com.google.android.material:material:1.6.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.8")

    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.google.android.exoplayer:exoplayer:2.15.0")
}

apply(plugin = "com.google.gms.google-services")
