package com.example.hidaymovie.model;

public class User {
    private String email;
    private String password;
    private String displayName;

    // Constructor với các tham số email, password, và displayName
    public User(String email, String password, String displayName) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
