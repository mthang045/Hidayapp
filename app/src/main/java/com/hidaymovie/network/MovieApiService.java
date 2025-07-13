package com.hidaymovie.network;

import com.hidaymovie.model.CreditResponse;
import com.hidaymovie.model.GenreResponse;
import com.hidaymovie.model.Movie;
import com.hidaymovie.model.MovieResponse;
import com.hidaymovie.model.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    // LẤY DANH SÁCH THỂ LOẠI
    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // LẤY PHIM THEO THỂ LOẠI
    @GET("discover/movie")
    Call<MovieResponse> discoverMoviesByGenre(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("with_genres") int genreId,
            @Query("page") int page
    );

    // CÁC API DANH SÁCH PHIM
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


    // CÁC API CHO MÀN HÌNH CHI TIẾT
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("append_to_response") String appendToResponse
    );

    @GET("movie/{movie_id}/credits")
    Call<CreditResponse> getMovieCredits(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}/similar")
    Call<MovieResponse> getSimilarMovies(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}