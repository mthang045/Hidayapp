package com.hidaymovie.network;

import com.hidaymovie.model.Movie;
import com.hidaymovie.model.Episode;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

import retrofit2.http.Headers;

public interface ApiService {

    /**
     * Lấy danh sách phim mới cập nhật (phân trang)
     * API: https://ophim1.com/danh-sach/phim-moi-cap-nhat?page={page}
     *
     * @param page số trang (bắt đầu từ 1)
     * @return Call object chứa dữ liệu raw JSON hoặc mapped object
     */
    @GET("danh-sach/phim-moi-cap-nhat")
    Call<Map<String, Object>> getNewMovies(
            @Query("page") int page
    );

    /**
     * Lấy chi tiết phim theo slug
     * API: https://ophim1.com/phim/{slug}
     *
     * @param slug định danh phim
     * @return Call object chứa dữ liệu raw JSON hoặc mapped object
     */
    @GET("phim/{slug}")
    Call<Map<String, Object>> getMovieDetail(
            @Path("slug") String slug
    );

    // Nếu API trả về cấu trúc dữ liệu rõ ràng bạn có thể thay Map<String,Object> bằng class model tương ứng
}
