package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("key")
    private String key; // Key của video trên YouTube

    @SerializedName("name")
    private String name; // Tên của video

    @SerializedName("site")
    private String site; // Nền tảng (vd: "YouTube")

    public String getKey() { return key; }

    public String getName() { return name; }

    public String getSite() { return site; }
}