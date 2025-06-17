package com.example.hidaymovie.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
public class MainActivity extends AppCompatActivity {

    private RecyclerView nowPlayingMoviesRecyclerView, popularMoviesRecyclerView, upcomingMoviesRecyclerView, topRatedMoviesRecyclerView;
    private MovieAdapter nowPlayingAdapter, popularAdapter, upcomingAdapter, topRatedAdapter;
    private static final String API_KEY = "d457a3503903f9582d34200aeddebdf5"; // Thay bằng API Key của bạn
    private static final String LANGUAGE = "en-US"; // Ngôn ngữ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView cho từng mục
        nowPlayingMoviesRecyclerView = findViewById(R.id.nowPlayingMoviesRecyclerView);
        popularMoviesRecyclerView = findViewById(R.id.popularMoviesRecyclerView);
        upcomingMoviesRecyclerView = findViewById(R.id.upcomingMoviesRecyclerView);
        topRatedMoviesRecyclerView = findViewById(R.id.topRatedMoviesRecyclerView);

        // Khởi tạo các Adapter cho từng RecyclerView
        nowPlayingAdapter = new MovieAdapter(this);
        popularAdapter = new MovieAdapter(this);
        upcomingAdapter = new MovieAdapter(this);
        topRatedAdapter = new MovieAdapter(this);

        // Thiết lập LayoutManager cho từng RecyclerView
        nowPlayingMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        upcomingMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topRatedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Gán Adapter cho các RecyclerView
        nowPlayingMoviesRecyclerView.setAdapter(nowPlayingAdapter);
        popularMoviesRecyclerView.setAdapter(popularAdapter);
        upcomingMoviesRecyclerView.setAdapter(upcomingAdapter);
        topRatedMoviesRecyclerView.setAdapter(topRatedAdapter);

        // Gọi API để lấy danh sách phim cho từng mục
        getNowPlayingMovies(1);   // Phim đang chiếu
        getPopularMovies(1);      // Phim phổ biến
        getUpcomingMovies(1);     // Phim sắp ra mắt
        getTopRatedMovies(1);     // Phim nổi bật
    }

    // API lấy phim đang chiếu
    private void getNowPlayingMovies(int page) {
        MovieApiService apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<MovieResponse> call = apiService.getNowPlayingMovies(API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    nowPlayingAdapter.setMovies(response.body().getResults());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // API lấy phim phổ biến
    private void getPopularMovies(int page) {
        MovieApiService apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    popularAdapter.setMovies(response.body().getResults());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // API lấy phim sắp ra mắt
    private void getUpcomingMovies(int page) {
        MovieApiService apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<MovieResponse> call = apiService.getUpcomingMovies(API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    upcomingAdapter.setMovies(response.body().getResults());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // API lấy phim nổi bật
    private void getTopRatedMovies(int page) {
        MovieApiService apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topRatedAdapter.setMovies(response.body().getResults());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
