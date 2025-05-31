package com.hidaymovie.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hidaymovie.R;
import com.hidaymovie.ui.auth.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextView tvDisplayName, tvEmail;
    private Button btnLogout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = findViewById(R.id.imgAvatar);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        loadUserInfo();

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvDisplayName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Người dùng");
            tvEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
            }
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
