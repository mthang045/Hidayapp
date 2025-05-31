package com.hidaymovie.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://ophim1.com/";

    private static Retrofit retrofit = null;

    // Trả về instance Retrofit singleton
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson để parse JSON
                    .build();
        }
        return retrofit;
    }

    // Trả về service API từ Retrofit
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
