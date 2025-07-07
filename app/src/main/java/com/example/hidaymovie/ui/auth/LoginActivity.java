package com.example.hidaymovie.ui.auth;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hidaymovie.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hidaymovie.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtInput, edtPassword;
    private Button btnSignIn;
    private TextView tvRegister, tvForgotPassword;
    private ImageView imgShowPassword;
    private CheckBox cbRememberMe;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtInput = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        // === PHẦN SỬA LỖI: THÊM LẠI CÁC SỰ KIỆN ===

        // 1. Xử lý "Ghi nhớ đăng nhập"
        loadRememberMe();

        // 2. Xử lý ẩn/hiện mật khẩu
        imgShowPassword.setOnClickListener(v -> togglePasswordVisibility());

        // 3. Xử lý nút "Đăng nhập"
        btnSignIn.setOnClickListener(v -> attemptLogin());

        // 4. Xử lý nút "Đăng ký"
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // 5. Xử lý nút "Quên mật khẩu"
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void loadRememberMe() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedInput = sharedPreferences.getString("input", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedInput.isEmpty() && !savedPassword.isEmpty()) {
            edtInput.setText(savedInput);
            edtPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            edtPassword.setInputType(129); // Kiểu mật khẩu (ẩn)
            imgShowPassword.setImageResource(R.drawable.ic_eyes_on);
        } else {
            edtPassword.setInputType(128); // Kiểu văn bản (hiện)
            // === SỬA LỖI TẠM THỜI ===
            // Sử dụng lại icon mắt nhắm để tránh lỗi biên dịch.
            // Giải pháp tốt nhất là tạo file ic_eyes_on.xml như hướng dẫn.
            imgShowPassword.setImageResource(R.drawable.ic_eyes_on);
        }
        isPasswordVisible = !isPasswordVisible;
        edtPassword.setSelection(edtPassword.length()); // Giữ con trỏ ở cuối
    }

    private void attemptLogin() {
        String input = edtInput.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (input.contains("@")) {
            signInWithEmail(input, password);
        } else {
            findEmailByUsernameAndSignIn(input, password);
        }
    }

    private void findEmailByUsernameAndSignIn(String username, String password) {
        db.collection("users")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            if (email != null) {
                                signInWithEmail(email, password);
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Xử lý lưu thông tin nếu "Ghi nhớ" được chọn
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (cbRememberMe.isChecked()) {
                            editor.putString("input", edtInput.getText().toString().trim());
                            editor.putString("password", edtPassword.getText().toString().trim());
                        } else {
                            editor.clear();
                        }
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
