package com.example.hidaymovie.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hidaymovie.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hidaymovie.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Khai báo các view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        // Xử lý sự kiện đăng ký
        btnSignUp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            createAccount(email, password);
        });

        // Quay lại màn hình đăng nhập
        tvLogin.setOnClickListener(v -> {
            finish(); // Quay lại trang đăng nhập
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            updateUI(user);
                        }
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Lưu thông tin người dùng vào Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Tạo đối tượng User từ thông tin FirebaseUser
            long currentTime = System.currentTimeMillis();
            User newUser = new User(user.getUid(), user.getEmail(), user.getDisplayName(),
                    user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null, currentTime, false);

            // Lưu vào Firestore
            db.collection("users").document(user.getUid())
                    .set(newUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(RegisterActivity.this, "Thông tin người dùng đã được lưu", Toast.LENGTH_SHORT).show();
                        // Chuyển tới màn hình đăng nhập hoặc trang chính
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // Hoặc MainActivity
                        startActivity(intent);
                        finish();  // Đảm bảo không quay lại RegisterActivity khi nhấn back
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
