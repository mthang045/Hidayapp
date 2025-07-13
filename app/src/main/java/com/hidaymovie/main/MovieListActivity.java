package com.hidaymovie.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hidaymovie.R;
import com.hidaymovie.adapter.MovieAdapter;
import com.hidaymovie.model.MovieResponse;
import com.hidaymovie.network.MovieApiService;
import com.hidaymovie.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    private static final String API_KEY = "e9e9d8da18ae29fc430845952232787c";
    private static final String LANGUAGE = "en-US";

    private RecyclerView rvMovieList;
    private MovieAdapter movieAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        toolbar = findViewById(R.id.toolbar);
        rvMovieList = findViewById(R.id.rv_movie_list);
        setupRecyclerView();

        // Ưu tiên lấy dữ liệu từ thể loại trước
        int genreId = getIntent().getIntExtra("genre_id", -1);
        String genreName = getIntent().getStringExtra("genre_name");

        if (genreId != -1 && genreName != null) {
            // Trường hợp: Mở từ click vào một thể loại
            setupToolbar(genreName);
            fetchMoviesByGenre(genreId, 1);
        } else {
            // Trường hợp cũ: Mở từ nút "Xem tất cả"
            String categoryTitle = getIntent().getStringExtra("category_title");
            String categoryType = getIntent().getStringExtra("category_type");
            setupToolbar(categoryTitle);
            if (categoryType != null) {
                fetchMoviesByCategory(categoryType, 1);
            }
        }
    }

    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(this);
        rvMovieList.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovieList.setAdapter(movieAdapter);
    }

    private void fetchMoviesByGenre(int genreId, int page) {
        MovieApiService apiService = RetrofitClient.getMovieApiService();
        apiService.discoverMoviesByGenre(API_KEY, LANGUAGE, genreId, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieAdapter.setMovies(response.body().getResults());
                } else {
                    Toast.makeText(MovieListActivity.this, "Không thể tải dữ liệu phim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Lỗi tải phim theo thể loại: " + t.getMessage());
                Toast.makeText(MovieListActivity.this, "Lỗi mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMoviesByCategory(String category, int page) {
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
                Toast.makeText(this, "Loại danh mục không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieAdapter.setMovies(response.body().getResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Toast.makeText(MovieListActivity.this, "Lỗi mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}