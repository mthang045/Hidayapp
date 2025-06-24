package com.example.hidaymovie.model;

public class User {
    private String email;
    private String displayName;
    private String profileImageUrl;  // Thêm URL ảnh đại diện

    // Constructor với các tham số email, displayName và profileImageUrl
    public User(String email, String displayName, String profileImageUrl) {
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
    }

    // Constructor không tham số
    public User() {
        // Có thể để trống hoặc đặt giá trị mặc định
    }

    // Getter và Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
