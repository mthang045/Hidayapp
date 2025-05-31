package com.hidaymovie.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hidaymovie.R;
import com.hidaymovie.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // Kiểm tra dữ liệu đầu vào
        if (!AuthUtils.isValidEmail(email)) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (!AuthUtils.isValidPassword(password)) {
            edtPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
            edtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Đăng nhập Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        // Chuyển đến MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
