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
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

}
dependencies {
    implementation("com.google.android.material:material:1.6.0") // Hoặc phiên bản mới nhất
}
dependencies {
    implementation("com.google.code.gson:gson:2.8.8")  // Thêm Gson vào project
}
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // Thêm Retrofit vào project
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")  // Nếu bạn sử dụng Gson converter (thường dùng với Retrofit)
}
dependencies {
    implementation("com.google.firebase:firebase-firestore:24.0.0")  // Thêm Firebase Firestore vào project
}
dependencies {
    implementation("com.github.bumptech.glide:glide:4.12.0")  // Thêm Glide vào project
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")  // Glide annotation processor
}
dependencies {
    implementation("com.google.firebase:firebase-auth:21.0.1") // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore:24.0.0") // Firebase Firestore (nếu cần lưu thông tin người dùng)
}

apply(plugin = "com.google.gms.google-services")
