package com.hidaymovie.network;

import com.hidaymovie.model.CreditResponse;
import com.hidaymovie.model.Movie;
import com.hidaymovie.model.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    // --- CÁC API CŨ GIỮ NGUYÊN ---
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("search/movie")
    Call<MovieResponse> searchMovies(@Query("api_key") String apiKey, @Query("query") String query, @Query("language") String language, @Query("page") int page);


    // --- CÁC API MỚI CHO MÀN HÌNH CHI TIẾT ---

    /**
     * Lấy thông tin chi tiết của một bộ phim, bao gồm cả danh sách video (trailers).
     * Sử dụng "append_to_response" để gộp 2 yêu cầu API làm 1.
     */
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("append_to_response") String appendToResponse // Sẽ truyền "videos" vào đây
    );

    /**
     * Lấy danh sách diễn viên của một bộ phim.
     */
    @GET("movie/{movie_id}/credits") // Endpoint đúng là "credits" không phải "casts"
    Call<CreditResponse> getMovieCredits(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );

    /**
     * Lấy danh sách review của một bộ phim.
     */
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Lấy danh sách các bộ phim tương tự.
     */
    @GET("movie/{movie_id}/similar")
    Call<MovieResponse> getSimilarMovies(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}