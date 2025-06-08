package com.example.hidaymovie.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hidaymovie.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hidaymovie.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hidaymovie.R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Khai báo các view
        edtEmail = findViewById(R.id.edtUsername);  // Đảm bảo id đúng
        edtPassword = findViewById(R.id.edtPassword);
       Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView tvRegister = findViewById(R.id.tvRegister);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập, chuyển đến MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Xử lý sự kiện đăng nhập
        btnSignIn.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        // Chuyển đến màn hình đăng ký
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        getUserData(user);  // Lấy dữ liệu người dùng từ Firestore
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserData(FirebaseUser user) {
        if (user != null) {
            // Lấy dữ liệu người dùng từ Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users") // Giả sử bạn lưu thông tin người dùng trong collection "users"
                    .document(user.getUid()) // Lấy thông tin người dùng dựa trên UID của Firebase
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lấy dữ liệu người dùng từ Firestore
                            if (task.getResult() != null) {
                                String username = task.getResult().getString("username");
                                String email = task.getResult().getString("email");

                                // Xử lý dữ liệu người dùng (ví dụ: hiển thị thông tin người dùng)
                                Log.d("UserData", "Username: " + username + ", Email: " + email);

                                // Chuyển đến MainActivity sau khi lấy dữ liệu người dùng
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Lỗi khi lấy dữ liệu
                            Log.w("LoginActivity", "Error getting user data", task.getException());
                        }
                    });
        }
    }
}
