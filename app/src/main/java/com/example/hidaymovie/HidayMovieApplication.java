package com.hidaymovie;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.hidaymovie.network.ApiClient;

public class HidayMovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Firebase (nếu chưa khởi tạo)
        FirebaseApp.initializeApp(this);

        // Khởi tạo Retrofit client (lazily)
        ApiClient.getClient();

        // Bạn có thể thêm khởi tạo các thành phần khác ở đây nếu cần
    }
}
