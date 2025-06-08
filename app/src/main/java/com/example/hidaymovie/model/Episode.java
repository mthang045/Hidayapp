package com.example.hidaymovie.model;

import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("title")
    private String title;        // Tên tập phim, ví dụ "Tập 1", "Episode 1"

    @SerializedName("slug")
    private String slug;         // Slug của tập phim (nếu API cung cấp)

    @SerializedName("server_data")
    private String videoUrl;     // URL video tập phim (đường dẫn phát)

    @SerializedName("episode_number")
    private int episodeNumber;   // Số thứ tự tập phim (nếu có)

    // Constructor
    public Episode(String title, String slug, String videoUrl, int episodeNumber) {
        this.title = title;
        this.slug = slug;
        this.videoUrl = videoUrl;
        this.episodeNumber = episodeNumber;
    }

    // Getter và Setter
    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    // Nếu cần setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
