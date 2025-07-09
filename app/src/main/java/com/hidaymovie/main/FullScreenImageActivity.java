package com.hidaymovie.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hidaymovie.R;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView fullScreenImageView;
    private FirebaseUser currentUser;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullScreenImageView = findViewById(R.id.fullScreenImageView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Load ảnh hiện tại (nếu có)
        String imageUrl = getIntent().getStringExtra("image_url");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(fullScreenImageView);
        } else {
            // Hiển thị ảnh mặc định nếu chưa có
            Glide.with(this).load(R.drawable.ic_profile).into(fullScreenImageView);
        }

        // Đăng ký trình khởi chạy để nhận kết quả từ thư viện ảnh
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        uploadImageToFirebase(imageUri);
                    }
                }
        );

        // Thêm sự kiện click vào chính ảnh đại diện
        fullScreenImageView.setOnClickListener(v -> showOptionsDialog());
    }

    // Hàm hiển thị dialog với các tùy chọn
    private void showOptionsDialog() {
        final CharSequence[] options = {"Chọn ảnh đại diện", "Hủy"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("Tùy chọn")
                .setItems(options, (dialog, item) -> {
                    if (options[item].equals("Chọn ảnh đại diện")) {
                        // Gọi hàm mở thư viện ảnh
                        openImageChooser();
                    } else if (options[item].equals("Hủy")) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Hàm mở thư viện ảnh để người dùng chọn
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    // Hàm tải ảnh lên Firebase Storage
    private void uploadImageToFirebase(Uri imageUri) {
        if (currentUser == null) return;
        Toast.makeText(this, "Đang tải ảnh lên...", Toast.LENGTH_SHORT).show();

        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profile_images/" + currentUser.getUid() + ".jpg");

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> profileImageRef.getDownloadUrl().addOnSuccessListener(this::updateUserProfile))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show());
    }

    // Hàm cập nhật hồ sơ người dùng với URL ảnh mới
    private void updateUserProfile(Uri profileImageUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(profileImageUrl).build();
        currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                // Đóng màn hình này để quay lại trang Profile
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
