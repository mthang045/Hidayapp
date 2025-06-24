package com.example.hidaymovie.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hidaymovie.R;
public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private TextView userNameTextView, userEmailTextView;
    private ImageView userProfileImageView;
    private Button editNameButton, editEmailButton, editPasswordButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // Lấy thông tin người dùng hiện tại từ Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Gắn các phần tử UI
        userNameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        userProfileImageView = findViewById(R.id.userProfileImage);
        editNameButton = findViewById(R.id.editNameButton);
        editEmailButton = findViewById(R.id.editEmailButton);
        editPasswordButton = findViewById(R.id.editPasswordButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Hiển thị thông tin người dùng
        if (user != null) {
            userNameTextView.setText(user.getDisplayName());
            userEmailTextView.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(userProfileImageView);
        }

        // Xử lý khi nhấn vào chỉnh sửa tên
        editNameButton.setOnClickListener(v -> {
            showEditNameDialog();
        });

        // Xử lý khi nhấn vào chỉnh sửa email
        editEmailButton.setOnClickListener(v -> {
            showEditEmailDialog();
        });

        // Xử lý khi nhấn vào chỉnh sửa mật khẩu
        editPasswordButton.setOnClickListener(v -> {
            showEditPasswordDialog();
        });

        // Xử lý đăng xuất
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Hàm để mở dialog chỉnh sửa tên người dùng
    private void showEditNameDialog() {
        // Ví dụ sử dụng AlertDialog để chỉnh sửa tên người dùng
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Edit Name");

        final EditText input = new EditText(ProfileActivity.this);
        input.setText(user.getDisplayName());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = input.getText().toString().trim();

            if (!TextUtils.isEmpty(newName)) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Name updated!", Toast.LENGTH_SHORT).show();
                                userNameTextView.setText(newName);
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Hàm để mở dialog chỉnh sửa email
    private void showEditEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Edit Email");

        final EditText input = new EditText(ProfileActivity.this);
        input.setText(user.getEmail());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newEmail = input.getText().toString().trim();

            if (!TextUtils.isEmpty(newEmail)) {
                user.updateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Email updated!", Toast.LENGTH_SHORT).show();
                                userEmailTextView.setText(newEmail);
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Hàm để mở dialog chỉnh sửa mật khẩu
    private void showEditPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Change Password");

        final EditText input = new EditText(ProfileActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Enter new password");
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newPassword = input.getText().toString().trim();

            if (newPassword.length() >= 6) {
                user.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Password updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ProfileActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
