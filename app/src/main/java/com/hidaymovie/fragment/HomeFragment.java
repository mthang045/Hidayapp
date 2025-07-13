package com.hidaymovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.hidaymovie.adapter.GenreAdapter;
import com.hidaymovie.adapter.MovieAdapter;
import com.hidaymovie.main.MovieListActivity;
import com.hidaymovie.model.GenreResponse;
import com.hidaymovie.model.MovieResponse;
import com.hidaymovie.network.MovieApiService;
import com.hidaymovie.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String API_KEY = "e9e9d8da18ae29fc430845952232787c"; // Thay bằng key của bạn
    private static final String LANGUAGE = "en-US";

    private RecyclerView rvGenres;
    private GenreAdapter genreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thiết lập danh mục thể loại
        setupGenreSection(view);
        fetchGenres();

        // Thiết lập các mục phim
        setupMovieSection(view, R.id.nowPlayingMoviesRecyclerView, R.id.pb_now_playing, R.id.tvSeeAllNowPlaying, "Phim Đang Chiếu", "now_playing");
        setupMovieSection(view, R.id.popularMoviesRecyclerView, R.id.pb_popular, R.id.tvSeeAllPopular, "Phim Phổ Biến", "popular");
        setupMovieSection(view, R.id.upcomingMoviesRecyclerView, R.id.pb_upcoming, R.id.tvSeeAllUpcoming, "Phim Sắp Ra Mắt", "upcoming");
        setupMovieSection(view, R.id.topRatedMoviesRecyclerView, R.id.pb_top_rated, R.id.tvSeeAllTopRated, "Phim Nổi Bật", "top_rated");
    }

    private void setupGenreSection(View view) {
        rvGenres = view.findViewById(R.id.rv_genres);
        genreAdapter = new GenreAdapter(getContext());
        rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenres.setAdapter(genreAdapter);
    }

    private void fetchGenres() {
        MovieApiService apiService = RetrofitClient.getMovieApiService();
        apiService.getMovieGenres(API_KEY, "vi-VN").enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    genreAdapter.setGenres(response.body().getGenres());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Log.e("API_ERROR", "Genre: " + t.getMessage());
                }
            }
        });
    }

    private void setupMovieSection(View view, int recyclerViewId, int progressBarId, int seeAllId, String title, String category) {
        RecyclerView recyclerView = view.findViewById(recyclerViewId);
        ProgressBar progressBar = view.findViewById(progressBarId);
        TextView tvSeeAll = view.findViewById(seeAllId);
        MovieAdapter movieAdapter = new MovieAdapter(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(movieAdapter);

        tvSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MovieListActivity.class);
            intent.putExtra("category_title", title);
            intent.putExtra("category_type", category);
            startActivity(intent);
        });

        fetchMovies(movieAdapter, progressBar, category, 1);
    }

    private void fetchMovies(MovieAdapter adapter, ProgressBar progressBar, String category, int page) {
        progressBar.setVisibility(View.VISIBLE);
        MovieApiService apiService = RetrofitClient.getMovieApiService();
        Call<MovieResponse> call;

        switch (category) {
            case "now_playing":
                call = apiService.getNowPlayingMovies(API_KEY, LANGUAGE, page);
                break;
            case "popular":
                call = apiService.getPopularMovies(API_KEY, LANGUAGE, page);
                break;
            case "upcoming":
                call = apiService.getUpcomingMovies(API_KEY, LANGUAGE, page);
                break;
            case "top_rated":
                call = apiService.getTopRatedMovies(API_KEY, LANGUAGE, page);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                return;
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        adapter.setMovies(response.body().getResults());
                    } else {
                        Toast.makeText(getContext(), "Không thể tải dữ liệu: " + category, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("API_ERROR", category + ": " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}