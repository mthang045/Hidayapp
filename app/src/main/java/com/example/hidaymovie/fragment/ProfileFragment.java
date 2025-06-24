package com.example.hidaymovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hidaymovie.ui.auth.LoginActivity;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hidaymovie.R;
import com.example.hidaymovie.main.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private ImageView userProfileImage;
    private TextView userName, userEmail;
    private Button editProfileImageButton, editNameButton, editEmailButton, editPasswordButton, logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userProfileImage = view.findViewById(R.id.userProfileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        editProfileImageButton = view.findViewById(R.id.editProfileImageButton);
        editNameButton = view.findViewById(R.id.editNameButton);
        editEmailButton = view.findViewById(R.id.editEmailButton);
        editPasswordButton = view.findViewById(R.id.editPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Hiển thị thông tin người dùng
        displayUserInfo();

        // Xử lý sự kiện các nút
        editProfileImageButton.setOnClickListener(v -> changeProfileImage());
        editNameButton.setOnClickListener(v -> changeUserName());
        editEmailButton.setOnClickListener(v -> changeUserEmail());
        editPasswordButton.setOnClickListener(v -> changeUserPassword());
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    // Hiển thị thông tin người dùng từ Firebase
    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());

            // Hiển thị ảnh đại diện từ Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/" + user.getUid() + ".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(getActivity())
                        .load(uri)
                        .into(userProfileImage);
            });
        }
    }

    // Thay đổi ảnh đại diện
    private void changeProfileImage() {
        // Logic thay đổi ảnh đại diện (chọn ảnh mới từ thư viện)
        // Ví dụ sử dụng Intent để chọn ảnh từ thư viện:
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100); // PICK_IMAGE_REQUEST = 100
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            // Chọn ảnh xong, tải lên Firebase Storage và cập nhật ảnh đại diện
            // Tải ảnh mới lên Firebase
            uploadProfileImage(data.getData());
        }
    }

    // Tải ảnh đại diện mới lên Firebase Storage
    private void uploadProfileImage(android.net.Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/" + user.getUid() + ".jpg");

            profileImageRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        profileImageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            Glide.with(getActivity())
                                    .load(downloadUri)
                                    .into(userProfileImage);

                            // Cập nhật URL ảnh vào Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid())
                                    .update("profileImageUrl", downloadUri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Chỉnh sửa tên người dùng
    private void changeUserName() {
        // Thêm logic để thay đổi tên người dùng
        // Ví dụ: Mở dialog để người dùng nhập tên mới
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Change Name")
                .setView(R.layout.dialog_edit_name) // Tạo một layout dialog để người dùng nhập tên
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = "New Name"; // Lấy tên từ EditText trong dialog
                    updateUserName(newName);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Cập nhật tên người dùng trong Firebase
    private void updateUserName(String newName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userName.setText(newName); // Cập nhật giao diện
                            Toast.makeText(getActivity(), "Name updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Chỉnh sửa email người dùng
    private void changeUserEmail() {
        // Mở dialog để nhập email mới
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Change Email")
                .setView(R.layout.dialog_edit_email) // Tạo layout dialog để người dùng nhập email mới
                .setPositiveButton("Save", (dialog, which) -> {
                    String newEmail = "newemail@example.com"; // Lấy email từ EditText trong dialog
                    updateUserEmail(newEmail);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Cập nhật email người dùng trong Firebase
    private void updateUserEmail(String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userEmail.setText(newEmail); // Cập nhật giao diện
                            Toast.makeText(getActivity(), "Email updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Chỉnh sửa mật khẩu
    private void changeUserPassword() {
        // Mở dialog hoặc màn hình thay đổi mật khẩu
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Change Password")
                .setView(R.layout.dialog_change_password) // Tạo layout dialog để người dùng nhập mật khẩu mới
                .setPositiveButton("Save", (dialog, which) -> {
                    String newPassword = "newPassword"; // Lấy mật khẩu mới từ dialog
                    updatePassword(newPassword);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Cập nhật mật khẩu người dùng trong Firebase
    private void updatePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Password updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Đăng xuất người dùng
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
