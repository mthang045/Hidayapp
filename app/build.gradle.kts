plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    // === ĐỔI VỀ PACKAGE NAME GỐC CỦA FIREBASE ===
    namespace = "com.hidaymovie"
    compileSdk = 33

    defaultConfig {
        // === ĐỔI VỀ PACKAGE NAME GỐC CỦA FIREBASE ===
        applicationId = "com.hidaymovie"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "TMDB_API_KEY", "\"d457a3503903f9582d34200aeddebdf5\"")
        }
        getByName("debug") {
            buildConfigField("String", "TMDB_API_KEY", "\"d457a3503903f9582d34200aeddebdf5\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Dán toàn bộ phần dependencies của bạn vào đây
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-firestore:24.0.0")
    implementation("com.google.firebase:firebase-storage:20.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("com.google.android.material:material:1.6.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.android.exoplayer:exoplayer:2.15.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0")
}
