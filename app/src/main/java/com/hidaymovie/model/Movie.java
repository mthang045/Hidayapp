package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {
    // --- Các trường cũ của bạn ---
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;

    // --- CÁC TRƯỜNG MỚI ĐƯỢC THÊM VÀO ---
    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("runtime")
    private int runtime; // Thời lượng phim (phút)

    // Đây là nơi chứa kết quả từ "append_to_response=videos"
    @SerializedName("videos")
    private VideoResponse videoResponse;


    // --- GETTERS & SETTERS CHO TẤT CẢ CÁC TRƯỜNG ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return poster_path; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    public String getBackdropPath() { return backdrop_path; }
    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public float getVoteAverage() { return voteAverage; }
    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }

    // Getters & Setters cho các trường mới
    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }

    public VideoResponse getVideoResponse() { return videoResponse; }
    public void setVideoResponse(VideoResponse videoResponse) { this.videoResponse = videoResponse; }
}