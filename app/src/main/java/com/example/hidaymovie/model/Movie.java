package com.example.hidaymovie.model;
public class Movie {
    public String title;
    public String backdrop_path;
    public String overview;  // Trường mô tả phim
    public String releaseDate;
    public float voteAverage;
    public String videoUrl;

    // Getter và Setter cho title, posterPath, overview, releaseDate, voteAverage, videoUrl
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return backdrop_path;
    }

    public void setPosterPath(String posterPath) {
        this.backdrop_path = posterPath;
    }

    public String getOverview() {
        return overview;  // Phương thức để lấy mô tả phim
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
