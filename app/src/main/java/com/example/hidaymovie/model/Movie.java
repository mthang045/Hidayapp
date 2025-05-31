package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {

    @SerializedName("title")
    private String title;           // Tên phim

    @SerializedName("slug")
    private String slug;            // Đường dẫn slug phim dùng để lấy chi tiết

    @SerializedName("thumb")        // Ảnh đại diện (thumbnail)
    private String thumb;

    @SerializedName("description")  // Mô tả ngắn
    private String description;

    @SerializedName("status")       // Trạng thái phim (đang chiếu, hoàn thành,...)
    private String status;

    @SerializedName("episode_current")   // Tập hiện tại (nếu có)
    private String episodeCurrent;

    @SerializedName("episode_total")     // Tổng số tập (nếu có)
    private int episodeTotal;

    @SerializedName("categories")        // Thể loại phim (danh sách)
    private List<String> categories;

    // Các getter và setter

    public Movie(String title, String slug, String thumb, String description, String status,
                 String episodeCurrent, int episodeTotal, List<String> categories) {
        this.title = title;
        this.slug = slug;
        this.thumb = thumb;
        this.description = description;
        this.status = status;
        this.episodeCurrent = episodeCurrent;
        this.episodeTotal = episodeTotal;
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getThumb() {
        return thumb;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getEpisodeCurrent() {
        return episodeCurrent;
    }

    public int getEpisodeTotal() {
        return episodeTotal;
    }

    public List<String> getCategories() {
        return categories;
    }

    // Bạn có thể thêm setter nếu cần thay đổi dữ liệu sau này
}
