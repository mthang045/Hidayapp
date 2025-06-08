package com.example.hidaymovie.ui.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hidaymovie.R;
import com.example.hidaymovie.model.Episode;
import com.example.hidaymovie.network.ApiClient;
import com.example.hidaymovie.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgThumbnail;
    private TextView tvTitle, tvDescription, tvStatus, tvEpisodes;
    private RecyclerView recyclerEpisodes;
    private ProgressBar progressBar;

    private com.example.hidaymovie.ui.detail.EpisodeAdapter episodeAdapter;
    private List<Episode> episodeList = new ArrayList<>();

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ánh xạ view
        imgThumbnail = findViewById(R.id.imgThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        tvEpisodes = findViewById(R.id.tvEpisodes);
        recyclerEpisodes = findViewById(R.id.recyclerEpisodes);
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView
        recyclerEpisodes.setLayoutManager(new LinearLayoutManager(this));
        episodeAdapter = new EpisodeAdapter(episodeList);
        recyclerEpisodes.setAdapter(episodeAdapter);

        // Khởi tạo API service
        apiService = ApiClient.getApiService();

        // Lấy slug phim truyền từ Intent
        String slug = getIntent().getStringExtra("slug");
        if (slug == null || slug.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Gọi API lấy chi tiết phim
        fetchMovieDetail(slug);
    }

    private void fetchMovieDetail(String slug) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getMovieDetail(slug).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();

                    // Lấy thông tin phim từ key "movie"
                    Map<String, Object> movie = (Map<String, Object>) data.get("movie");
                    if (movie == null) {
                        Toast.makeText(DetailActivity.this, "Không lấy được dữ liệu phim", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Lấy các trường dữ liệu cần thiết
                    String title = (String) movie.get("title");
                    String description = (String) movie.get("description");
                    String status = (String) movie.get("status");
                    String thumb = (String) movie.get("thumb");
                    String episodeCurrent = (String) movie.get("episode_current");

                    // Hiển thị lên UI
                    tvTitle.setText(title);
                    tvDescription.setText(description);
                    tvStatus.setText("Trạng thái: " + status);
                    tvEpisodes.setText("Tập hiện tại: " + episodeCurrent);

                    Glide.with(DetailActivity.this)
                            .load(thumb)
                            .placeholder(R.drawable.ic_placeholder) // bạn cần có drawable này
                            .into(imgThumbnail);

                    // Lấy danh sách tập phim (giả sử key "episodes" là List)
                    List<Map<String, Object>> episodes = (List<Map<String, Object>>) movie.get("episodes");
                    if (episodes != null) {
                        episodeList.clear();
                        for (Map<String, Object> ep : episodes) {
                            String epTitle = (String) ep.get("title");
                            String epSlug = (String) ep.get("slug");

                            String videoUrl = "";
                            Object serverData = ep.get("server_data");
                            if (serverData instanceof String) {
                                videoUrl = (String) serverData;
                            }
                            // Nếu server_data phức tạp hơn bạn cần parse thêm

                            int epNumber = ep.get("episode_number") instanceof Number ?
                                    ((Number) ep.get("episode_number")).intValue() : 0;

                            episodeList.add(new Episode(epTitle, epSlug, videoUrl, epNumber));
                        }
                        episodeAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(DetailActivity.this, "Lỗi tải dữ liệu phim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
