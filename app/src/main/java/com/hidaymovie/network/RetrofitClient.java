package com.hidaymovie.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    // Giữ lại 'retrofit' để đảm bảo chỉ có một đối tượng Retrofit duy nhất được tạo ra (Singleton)
    private static Retrofit retrofit = null;

    /**
     * Phương thức này tạo và trả về một đối tượng Retrofit duy nhất.
     * Nếu đã được tạo, nó sẽ trả về đối tượng cũ để tiết kiệm tài nguyên.
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Phương thức hỗ trợ tiện lợi: trực tiếp trả về một thể hiện của MovieApiService.
     * Giúp mã ở các nơi khác (Activity, ViewModel) gọn gàng hơn.
     */
    public static MovieApiService getMovieApiService() {
        return getRetrofitInstance().create(MovieApiService.class);
    }
}