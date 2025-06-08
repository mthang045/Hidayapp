package com.example.hidaymovie.ui.main;

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
import com.example.hidaymovie.model.Movie;
import com.example.hidaymovie.network.ApiClient;
import com.example.hidaymovie.network.ApiService;
import com.example.hidaymovie.ui.auth.LoginActivity;
import com.example.hidaymovie.ui.detail.DetailActivity;
import com.example.hidaymovie.ui.profile.ProfileActivity;
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
    private int currentPage = 1;

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
                    Map<String, Object> root = response.body();

                    // Lấy danh sách phim từ key "items"
                    List<Map<String, Object>> items = (List<Map<String, Object>>) root.get("items");
                    if (items != null && !items.isEmpty()) {
                        movieList.clear(); // Xóa danh sách cũ nếu có

                        // Lấy đường dẫn gốc ảnh
                        String pathImage = (String) root.get("pathImage");
                        if (pathImage == null) pathImage = "";

                        for (Map<String, Object> item : items) {
                            String title = (String) item.get("name");
                            String slug = (String) item.get("slug");
                            String thumbUrl = (String) item.get("thumb_url");
                            String description = (String) item.getOrDefault("origin_name", "");
                            String status = ""; // API không có status rõ, để trống hoặc xử lý khác
                            String episodeCurrent = ""; // Không có trường này trong JSON mẫu
                            int episodeTotal = 0; // Không có trường này trong JSON mẫu
                            List<String> categories = new ArrayList<>(); // Không có categories trong JSON mẫu

                            String thumb = pathImage + thumbUrl;

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

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("slug", movie.getSlug());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setQueryHint("Tìm phim...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
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
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
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
