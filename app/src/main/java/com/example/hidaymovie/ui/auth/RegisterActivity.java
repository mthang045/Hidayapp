package com.hidaymovie.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hidaymovie.R;
import com.hidaymovie.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword, edtDisplayName;
    private Button btnRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtDisplayName = findViewById(R.id.edtDisplayName);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        String displayName = edtDisplayName.getText().toString().trim();

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

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!AuthUtils.isValidDisplayName(displayName)) {
            edtDisplayName.setError("Tên hiển thị phải ít nhất 3 ký tự");
            edtDisplayName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Đăng ký thành công, cập nhật tên hiển thị
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                            .setDisplayName(displayName)
                                            .build())
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            // Chuyển sang MainActivity
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Cập nhật tên hiển thị thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
