package com.example.hidaymovie;


import android.app.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HidayMovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo FirebaseAuth và FirebaseFirestore chỉ một lần khi ứng dụng khởi động
        FirebaseAuth.getInstance();
        FirebaseFirestore.getInstance();

        // Bạn có thể khởi tạo bất kỳ dịch vụ hoặc cấu hình nào khác ở đây
        // Ví dụ: Theo dõi người dùng, thiết lập các dịch vụ toàn cục, v.v.
    }
}
