package com.hidaymovie.model; // Thay bằng package của bạn

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() { return id; }
    public String getName() { return name; }
}