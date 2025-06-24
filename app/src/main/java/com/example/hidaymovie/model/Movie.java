package com.example.hidaymovie.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    private int id;

    // Đổi các trường thành private để nhất quán
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


    // === GETTERS ===
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterPath() { return poster_path; }
    public String getBackdropPath() { return backdrop_path; }
    public String getOverview() { return overview; }
    public String getReleaseDate() { return releaseDate; }
    public float getVoteAverage() { return voteAverage; }

    // === SETTERS (PHẦN CẦN THIẾT ĐỂ SỬA LỖI) ===
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }
    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }
}
