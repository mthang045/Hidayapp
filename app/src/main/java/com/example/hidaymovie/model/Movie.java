package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {

    @SerializedName("name")
    private String title;

    @SerializedName("slug")
    private String slug;

    @SerializedName("thumb_url")
    private String thumb;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("episode_current")
    private String episodeCurrent;

    @SerializedName("episode_total")
    private Integer episodeTotal;

    @SerializedName("categories")
    private List<String> categories;  // Thêm thuộc tính categories

    public Movie(String title, String slug, String thumb, String description, String status,
                 String episodeCurrent, Integer episodeTotal, List<String> categories) {
        this.title = title;
        this.slug = slug;
        this.thumb = thumb;
        this.description = description;
        this.status = status;
        this.episodeCurrent = episodeCurrent;
        this.episodeTotal = episodeTotal;
        this.categories = categories;
    }

    // Getter cho categories
    public List<String> getCategories() {
        return categories;
    }

    // Các getter còn lại giữ nguyên
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getThumb() { return thumb; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getEpisodeCurrent() { return episodeCurrent; }
    public Integer getEpisodeTotal() { return episodeTotal; }
}
