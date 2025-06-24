package com.example.hidaymovie.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidaymovie.adapter.MovieAdapter;
import com.example.hidaymovie.network.MovieApiService;
import com.example.hidaymovie.network.MovieResponse;
import com.example.hidaymovie.network.RetrofitClient;
import com.hidaymovie.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // Khai báo các RecyclerView và Adapter tương ứng
    private RecyclerView nowPlayingMoviesRecyclerView, popularMoviesRecyclerView, upcomingMoviesRecyclerView, topRatedMoviesRecyclerView;
    private MovieAdapter nowPlayingAdapter, popularAdapter, upcomingAdapter, topRatedAdapter;

    // Khóa API và ngôn ngữ
    private static final String API_KEY = "d457a3503903f9582d34200aeddebdf5";
    private static final String LANGUAGE = "en-US";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout cho Fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các RecyclerView từ layout
        nowPlayingMoviesRecyclerView = view.findViewById(R.id.nowPlayingMoviesRecyclerView);
        popularMoviesRecyclerView = view.findViewById(R.id.popularMoviesRecyclerView);
        upcomingMoviesRecyclerView = view.findViewById(R.id.upcomingMoviesRecyclerView);
        topRatedMoviesRecyclerView = view.findViewById(R.id.topRatedMoviesRecyclerView);

        // Khởi tạo các Adapter
        nowPlayingAdapter = new MovieAdapter(getContext());
        popularAdapter = new MovieAdapter(getContext());
        upcomingAdapter = new MovieAdapter(getContext());
        topRatedAdapter = new MovieAdapter(getContext());

        // Thiết lập LayoutManager cho từng RecyclerView (hiển thị theo chiều ngang)
        nowPlayingMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        upcomingMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topRatedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Gán Adapter cho từng RecyclerView
        nowPlayingMoviesRecyclerView.setAdapter(nowPlayingAdapter);
        popularMoviesRecyclerView.setAdapter(popularAdapter);
        upcomingMoviesRecyclerView.setAdapter(upcomingAdapter);
        topRatedMoviesRecyclerView.setAdapter(topRatedAdapter);

        // Gọi API để lấy dữ liệu cho cả 4 mục
        getNowPlayingMovies(1);
        getPopularMovies(1);
        getUpcomingMovies(1);
        getTopRatedMovies(1);
    }

    // Hàm lấy danh sách phim đang chiếu
    private void getNowPlayingMovies(int page) {
        RetrofitClient.getMovieApiService().getNowPlayingMovies(API_KEY, LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    nowPlayingAdapter.setMovies(response.body().getResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if(isAdded()) Log.e("API_ERROR", "Now Playing: " + t.getMessage());
            }
        });
    }

    // Hàm lấy danh sách phim phổ biến
    private void getPopularMovies(int page) {
        RetrofitClient.getMovieApiService().getPopularMovies(API_KEY, LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    popularAdapter.setMovies(response.body().getResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if(isAdded()) Log.e("API_ERROR", "Popular: " + t.getMessage());
            }
        });
    }

    // Hàm lấy danh sách phim sắp ra mắt
    private void getUpcomingMovies(int page) {
        RetrofitClient.getMovieApiService().getUpcomingMovies(API_KEY, LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    upcomingAdapter.setMovies(response.body().getResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if(isAdded()) Log.e("API_ERROR", "Upcoming: " + t.getMessage());
            }
        });
    }

    // Hàm lấy danh sách phim được đánh giá cao
    private void getTopRatedMovies(int page) {
        RetrofitClient.getMovieApiService().getTopRatedMovies(API_KEY, LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    topRatedAdapter.setMovies(response.body().getResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if(isAdded()) Log.e("API_ERROR", "Top Rated: " + t.getMessage());
            }
        });
    }
}
