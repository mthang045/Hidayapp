package com.example.hidaymovie.fragment;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hidaymovie.main.HistoryActivity;
import com.example.hidaymovie.ui.auth.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hidaymovie.R;

public class ProfileFragment extends Fragment {

    private ImageView userProfileImage;
    private TextView userName, userEmail;
    private Button editNameButton, historyButton, editPasswordButton, logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Ánh xạ các view từ layout
        userProfileImage = view.findViewById(R.id.userProfileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        editNameButton = view.findViewById(R.id.editNameButton);
        historyButton = view.findViewById(R.id.historyButton);
        editPasswordButton = view.findViewById(R.id.editPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Hiển thị thông tin người dùng
        displayUserInfo();

        // Cài đặt sự kiện cho các nút
        editNameButton.setOnClickListener(v -> changeUserName());
        editPasswordButton.setOnClickListener(v -> changeUserPassword());
        logoutButton.setOnClickListener(v -> logout());

        // Sự kiện cho nút Lịch sử xem phim
        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HistoryActivity.class));
        });
    }

    // Hàm hiển thị thông tin người dùng
    private void displayUserInfo() {
        if (currentUser != null) {
            userName.setText(currentUser.getDisplayName());
            userEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile)
                        .into(userProfileImage);
            }
        }
    }

    // Hàm thay đổi tên người dùng
    private void changeUserName() {
        if (getContext() == null) return;
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_name, null);
        EditText editName = dialogView.findViewById(R.id.editName);
        if (currentUser != null) {
            editName.setText(currentUser.getDisplayName());
        }

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Đổi tên hiển thị")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = editName.getText().toString().trim();
                    if (!TextUtils.isEmpty(newName) && currentUser != null) {
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

    // Hàm thay đổi mật khẩu
    private void changeUserPassword() {
        if (getContext() == null) return;
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

                    if (currentUser != null && currentUser.getEmail() != null) {
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
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Hàm đăng xuất
    private void logout() {
        if (getActivity() == null) return;
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
