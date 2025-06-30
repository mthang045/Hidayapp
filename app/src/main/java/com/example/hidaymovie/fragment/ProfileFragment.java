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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hidaymovie.R;
import com.example.hidaymovie.main.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.hidaymovie.ui.auth.LoginActivity;
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

        displayUserInfo();

        editProfileImageButton.setOnClickListener(v -> changeProfileImage());
        editNameButton.setOnClickListener(v -> changeUserName());

        editPasswordButton.setOnClickListener(v -> changeUserPassword());
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            Glide.with(getActivity())
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.ic_profile)
                    .into(userProfileImage);
        }
    }

    private void changeProfileImage() {
        // Placeholder for future implementation
    }

    private void changeUserName() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_name, null);
        EditText editName = dialogView.findViewById(R.id.editName);

        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Change Name")
                .setView(dialogView)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newName = editName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(newName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            userName.setText(newName);
                                            Toast.makeText(getActivity(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Failed to update name", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void changeUserPassword() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_password, null);
        EditText currentPassword = dialogView.findViewById(R.id.editCurrentPassword);
        EditText newPassword = dialogView.findViewById(R.id.editNewPassword);
        EditText confirmPassword = dialogView.findViewById(R.id.editConfirmNewPassword);

        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Change Password")
                .setView(dialogView)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String current = currentPassword.getText().toString();
                    String newPass = newPassword.getText().toString();
                    String confirm = confirmPassword.getText().toString();

                    if (TextUtils.isEmpty(current) || newPass.length() < 6 || !newPass.equals(confirm)) {
                        Toast.makeText(getActivity(), "Please check your inputs", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null && user.getEmail() != null) {
                        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), current))
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPass)
                                                .addOnCompleteListener(updateTask -> {
                                                    if (updateTask.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getActivity(), "Re-authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
