package com.example.hidaymovie.main;

public class Review {
    public String username;
    public String content;
    public float rating;
    public String timestamp;  // ✅ sửa kiểu dữ liệu từ long → String

    public Review() {}

    public Review(String username, String content, float rating, String timestamp) {
        this.username = username;
        this.content = content;
        this.rating = rating;
        this.timestamp = timestamp;
    }
}
