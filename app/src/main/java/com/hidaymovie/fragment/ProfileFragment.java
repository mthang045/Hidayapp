package com.hidaymovie.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hidaymovie.R;
import com.hidaymovie.main.FullScreenImageActivity;
import com.hidaymovie.main.HistoryActivity;
import com.hidaymovie.ui.auth.LoginActivity;

public class ProfileFragment extends Fragment {

    private ImageView userProfileImage;
    private TextView userName, userEmail;
    private Button editNameButton, historyButton, editPasswordButton, btnChangeTheme, logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại thông tin người dùng khi quay lại Fragment này
        currentUser = mAuth.getCurrentUser();
        displayUserInfo();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Ánh xạ views
        userProfileImage = view.findViewById(R.id.userProfileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        editNameButton = view.findViewById(R.id.editNameButton);
        historyButton = view.findViewById(R.id.historyButton);
        editPasswordButton = view.findViewById(R.id.editPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        btnChangeTheme = view.findViewById(R.id.btn_change_theme);

        displayUserInfo();

        // Cài đặt sự kiện cho các nút
        setupClickListeners();
    }

    private void setupClickListeners() {
        userProfileImage.setOnClickListener(v -> showProfileImageOptions());
        editNameButton.setOnClickListener(v -> changeUserName());
        historyButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), HistoryActivity.class)));
        editPasswordButton.setOnClickListener(v -> changeUserPassword());
        logoutButton.setOnClickListener(v -> logout());
        btnChangeTheme.setOnClickListener(v -> showThemeDialog());
    }

    private void showThemeDialog() {
        if (getContext() == null) return;
        final String[] themes = {"Sáng", "Tối", "Theo hệ thống"};

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Chọn giao diện")
                .setItems(themes, (dialog, which) -> {
                    switch (which) {
                        case 0: // Chế độ Sáng
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case 1: // Chế độ Tối
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2: // Theo cài đặt hệ thống
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                })
                .show();
    }

    private void displayUserInfo() {
        if (currentUser != null) {
            String nameToDisplay = currentUser.getDisplayName();
            if (nameToDisplay == null || nameToDisplay.isEmpty()) {
                nameToDisplay = "Người dùng";
            }
            userName.setText(nameToDisplay);
            userEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this).load(currentUser.getPhotoUrl()).circleCrop().placeholder(R.drawable.ic_profile).into(userProfileImage);
            } else {
                Glide.with(this).load(R.drawable.ic_profile).circleCrop().into(userProfileImage);
            }
        }
    }

    private void showProfileImageOptions() {
        if (getContext() == null) return;
        final CharSequence[] options = {"Xem ảnh đại diện", "Chọn ảnh đại diện", "Hủy"};
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Ảnh đại diện")
                .setItems(options, (dialog, item) -> {
                    if (options[item].equals("Xem ảnh đại diện")) {
                        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
                        if (currentUser != null && currentUser.getPhotoUrl() != null) {
                            intent.putExtra("image_url", currentUser.getPhotoUrl().toString());
                        }
                        startActivity(intent);
                    } else if (options[item].equals("Chọn ảnh đại diện")) {
                        openImageChooser();
                    } else if (options[item].equals("Hủy")) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (currentUser == null) return;
        Toast.makeText(getContext(), "Đang tải ảnh lên...", Toast.LENGTH_SHORT).show();
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profile_images/" + currentUser.getUid() + ".jpg");
        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> profileImageRef.getDownloadUrl().addOnSuccessListener(this::updateUserProfile))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show());
    }

    private void updateUserProfile(Uri profileImageUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(profileImageUrl).build();
        currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                if (isAdded()) {
                    Glide.with(this).load(profileImageUrl).circleCrop().into(userProfileImage);
                }
            }
        });
    }

    private void changeUserName() {
        if (getContext() == null || currentUser == null) return;
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_name, null);
        EditText editName = dialogView.findViewById(R.id.editName);
        editName.setText(currentUser.getDisplayName());

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Đổi tên hiển thị")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = editName.getText().toString().trim();
                    if (!TextUtils.isEmpty(newName)) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newName)
                                .build();
                        currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                userName.setText(newName);
                                Toast.makeText(getContext(), "Cập nhật tên thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void changeUserEmail() {
        if (getContext() == null || currentUser == null) return;
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_email, null);
        EditText editNewEmail = dialogView.findViewById(R.id.editNewEmail);
        EditText editCurrentPassword = dialogView.findViewById(R.id.editCurrentPassword);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Đổi địa chỉ Email")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newEmail = editNewEmail.getText().toString().trim();
                    String password = editCurrentPassword.getText().toString();

                    if (TextUtils.isEmpty(newEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                        Toast.makeText(getContext(), "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getContext(), "Vui lòng nhập mật khẩu để xác thực", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), password))
                            .addOnCompleteListener(reauthTask -> {
                                if (reauthTask.isSuccessful()) {
                                    currentUser.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            userEmail.setText(newEmail);
                                            Toast.makeText(getContext(), "Cập nhật email thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Lỗi: Không thể cập nhật email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Xác thực thất bại, mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void changeUserPassword() {
        if (getContext() == null || currentUser == null) return;
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);
        EditText currentPassword = dialogView.findViewById(R.id.editCurrentPassword);
        EditText newPassword = dialogView.findViewById(R.id.editNewPassword);
        EditText confirmPassword = dialogView.findViewById(R.id.editConfirmNewPassword);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Đổi mật khẩu")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String current = currentPassword.getText().toString();
                    String newPass = newPassword.getText().toString();
                    String confirm = confirmPassword.getText().toString();

                    if (TextUtils.isEmpty(current)) {
                        Toast.makeText(getContext(), "Vui lòng nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPass.length() < 6) {
                        Toast.makeText(getContext(), "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPass.equals(confirm)) {
                        Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), current))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    currentUser.updatePassword(newPass).addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Xác thực thất bại, mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void logout() {
        if (getActivity() == null) return;
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}