package com.hidaymovie.model;

public class User {

    private String uid;          // ID người dùng do Firebase cấp
    private String email;        // Email người dùng
    private String displayName;  // Tên hiển thị
    private String photoUrl;     // URL ảnh đại diện (nếu có)
    private long createdAt;      // Thời gian tạo tài khoản (timestamp)
    private boolean isPremium;   // Trạng thái user có trả phí hay không (ví dụ)

    // Constructor không tham số (bắt buộc khi dùng Firebase)
    public User() {
    }

    // Constructor đầy đủ tham số
    public User(String uid, String email, String displayName, String photoUrl, long createdAt, boolean isPremium) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.isPremium = isPremium;
    }

    // Getter và Setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
