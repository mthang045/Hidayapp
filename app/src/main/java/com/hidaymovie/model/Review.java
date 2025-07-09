package com.hidaymovie.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Review {
    private String author;
    private String content;
    private float rating; // Điểm sao người dùng vote
    private String userId;
    private String userName;
    private String userAvatarUrl;

    @ServerTimestamp // Tự động lấy thời gian từ server Firebase
    private Date timestamp;

    // Constructor rỗng bắt buộc cho Firestore
    public Review() {}

    // Constructor để tạo review mới
    public Review(String author, String content, float rating, String userId, String userName, String userAvatarUrl) {
        this.author = author; // Giữ lại author từ TMDB nếu cần, nhưng sẽ ưu tiên userName
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
    }

    // Getters
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public float getRating() { return rating; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserAvatarUrl() { return userAvatarUrl; }
    public Date getTimestamp() { return timestamp; }

    // Setters (cần thiết cho Firestore)
    public void setAuthor(String author) { this.author = author; }
    public void setContent(String content) { this.content = content; }
    public void setRating(float rating) { this.rating = rating; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
