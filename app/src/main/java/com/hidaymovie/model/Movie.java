package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath; // Tên biến đúng
    @SerializedName("backdrop_path")
    private String backdropPath; // Tên biến đúng
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("genres")
    private List<Genre> genres;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("videos")
    private VideoResponse videoResponse;

    // Hàm khởi tạo rỗng BẮT BUỘC cho Firestore
    public Movie() {}

    // Getters và Setters theo chuẩn camelCase
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public float getVoteAverage() { return voteAverage; }
    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }

    public VideoResponse getVideoResponse() { return videoResponse; }
    public void setVideoResponse(VideoResponse videoResponse) { this.videoResponse = videoResponse; }
}