package com.hidaymovie.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hidaymovie.R;
import com.hidaymovie.ui.auth.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);

        // Load trạng thái hiện tại (ví dụ dùng SharedPreferences)
        boolean notificationsEnabled = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        switchNotifications.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            // Lưu trạng thái
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                    .putBoolean("notifications_enabled", isChecked)
                    .apply();

            Toast.makeText(this, "Thông báo " + (isChecked ? "bật" : "tắt"), Toast.LENGTH_SHORT).show();
        });
    }

    // Ví dụ thêm nút đăng xuất trong menu hoặc giao diện, xử lý như sau:
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
