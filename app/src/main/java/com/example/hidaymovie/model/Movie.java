package com.example.hidaymovie.model;

public class Movie {

    private String title;
    private String posterPath;
    private String videoUrl; // Thêm thuộc tính videoUrl

    // Constructor
    public Movie(String title, String posterPath, String videoUrl) {
        this.title = title;
        this.posterPath = posterPath;
        this.videoUrl = videoUrl;
    }

    // Getter và Setter cho title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter và Setter cho posterPath
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    // Getter và Setter cho videoUrl
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
