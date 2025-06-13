package com.example.hidaymovie.ui.auth;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.hidaymovie.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnSignIn;
    private TextView tvRegister, tvForgotPassword;
    private ImageView imgShowPassword;
    private CheckBox cbRememberMe;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe); // Bind CheckBox for Remember Me

        // Check if user credentials are saved in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            edtUsername.setText(savedEmail);
            edtPassword.setText(savedPassword);
            cbRememberMe.setChecked(true); // Automatically check Remember Me if credentials exist
        }

        // Toggle password visibility when user clicks on the eye icon
        imgShowPassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                edtPassword.setInputType(129); // PASSWORD type (show password)
                imgShowPassword.setImageResource(R.drawable.ic_eyes_off); // Show the eye icon
            } else {
                edtPassword.setInputType(128); // TEXT type (hide password)
                imgShowPassword.setImageResource(R.drawable.ic_eyes_off); // Show the crossed eye icon
            }
            isPasswordVisible = !isPasswordVisible;
        });

        // Sign In button click listener
        btnSignIn.setOnClickListener(v -> signInUser());

        // Register click listener to navigate to Register Activity
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Forgot password click listener
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void signInUser() {
        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Check if fields are empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // If Remember Me is checked, save user credentials in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (cbRememberMe.isChecked()) {
                            editor.putString("email", email);
                            editor.putString("password", password);
                        } else {
                            // Remove saved credentials if Remember Me is unchecked
                            editor.remove("email");
                            editor.remove("password");
                        }
                        editor.apply();

                        // Navigate to the main screen after successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close LoginActivity to prevent going back
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
