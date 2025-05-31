package com.hidaymovie.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.hidaymovie.model.Movie;
import com.hidaymovie.network.ApiClient;
import com.hidaymovie.network.ApiService;
import com.hidaymovie.ui.auth.LoginActivity;
import com.hidaymovie.ui.detail.DetailActivity;
import com.hidaymovie.ui.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private RecyclerView recyclerMovies;
    private ProgressBar progressBar;

    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    private ApiService apiService;
    private int currentPage = 1;  // Trang hiện tại, có thể nâng cấp pagination

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerMovies = findViewById(R.id.recyclerMovies);
        progressBar = findViewById(R.id.progressBar);

        recyclerMovies.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(movieList);
        movieAdapter.setOnMovieClickListener(this);
        recyclerMovies.setAdapter(movieAdapter);

        apiService = ApiClient.getApiService();

        fetchNewMovies(currentPage);
    }

    private void fetchNewMovies(int page) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        apiService.getNewMovies(page).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(ProgressBar.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();

                    // Lấy danh sách phim trong key "data" hoặc theo cấu trúc API thực tế
                    List<Map<String, Object>> moviesData = (List<Map<String, Object>>) data.get("data");
                    if (moviesData != null) {
                        for (Map<String, Object> item : moviesData) {
                            String title = (String) item.get("title");
                            String slug = (String) item.get("slug");
                            String thumb = (String) item.get("thumb");
                            String description = (String) item.get("description");
                            String status = (String) item.get("status");
                            String episodeCurrent = (String) item.get("episode_current");
                            int episodeTotal = item.get("episode_total") instanceof Number ?
                                    ((Number) item.get("episode_total")).intValue() : 0;
                            List<String> categories = (List<String>) item.get("categories");

                            Movie movie = new Movie(title, slug, thumb, description, status,
                                    episodeCurrent, episodeTotal, categories);
                            movieList.add(movie);
                        }
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Không có phim mới", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi tải phim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(MainActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Bắt sự kiện click phim, mở DetailActivity
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("slug", movie.getSlug());
        startActivity(intent);
    }

    // Xử lý menu: tìm kiếm, profile, đăng xuất
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Thiết lập SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setQueryHint("Tìm phim...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO: Thực hiện gọi API tìm kiếm hoặc lọc danh sách phim
                    Toast.makeText(MainActivity.this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Có thể thực hiện lọc danh sách theo text nhập từng ký tự nếu muốn
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            // Mở màn hình Profile
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            // Đăng xuất Firebase và chuyển về Login
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
