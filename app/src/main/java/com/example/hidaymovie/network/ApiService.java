package com.hidaymovie.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.Map;

public interface ApiService {

    /**
     * Lấy danh sách phim mới cập nhật
     * Endpoint: GET danh-sach/phim-moi-cap-nhat?page={page}
     * Dữ liệu trả về JSON chứa trường "items" là danh sách phim
     */
    @GET("danh-sach/phim-moi-cap-nhat")
    Call<Map<String, Object>> getNewMovies(@Query("page") int page);

    /**
     * Lấy chi tiết phim theo slug
     * Endpoint: GET phim/{slug}
     */
    @GET("phim/{slug}")
    Call<Map<String, Object>> getMovieDetail(@Path("slug") String slug);
}