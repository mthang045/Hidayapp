package com.hidaymovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CreditResponse {
    @SerializedName("cast")
    private List<Cast> cast;

    public List<Cast> getCast() {
        return cast;
    }
}