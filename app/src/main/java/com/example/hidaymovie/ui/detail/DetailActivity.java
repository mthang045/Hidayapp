package com.hidaymovie.ui.detail;

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
import com.hidaymovie.model.Episode;
import com.hidaymovie.network.ApiClient;
import com.hidaymovie.network.ApiService;

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

    private EpisodeAdapter episodeAdapter;
    private List<Episode> episodeList = new ArrayList<>();

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgThumbnail = findViewById(R.id.imgThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        tvEpisodes = findViewById(R.id.tvEpisodes);
        recyclerEpisodes = findViewById(R.id.recyclerEpisodes);
        progressBar = findViewById(R.id.progressBar);

        recyclerEpisodes.setLayoutManager(new LinearLayoutManager(this));
        episodeAdapter = new EpisodeAdapter(episodeList);
        recyclerEpisodes.setAdapter(episodeAdapter);

        apiService = ApiClient.getApiService();

        // Lấy slug truyền từ intent
        String slug = getIntent().getStringExtra("slug");
        if (slug == null || slug.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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

                    // Phân tích JSON theo cấu trúc thực tế API ophim1.com
                    // Giả sử data chứa key "movie" là thông tin phim
                    Map<String, Object> movie = (Map<String, Object>) data.get("movie");
                    if (movie == null) {
                        Toast.makeText(DetailActivity.this, "Không lấy được dữ liệu phim", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String title = (String) movie.get("title");
                    String description = (String) movie.get("description");
                    String status = (String) movie.get("status");
                    String thumb = (String) movie.get("thumb");
                    String episodeCurrent = (String) movie.get("episode_current");

                    // Hiển thị thông tin phim
                    tvTitle.setText(title);
                    tvDescription.setText(description);
                    tvStatus.setText("Trạng thái: " + status);
                    tvEpisodes.setText("Tập hiện tại: " + episodeCurrent);

                    Glide.with(DetailActivity.this)
                            .load(thumb)
                            .placeholder(R.drawable.ic_placeholder)
                            .into(imgThumbnail);

                    // Lấy danh sách tập phim (giả sử key "episodes" chứa list tập)
                    List<Map<String, Object>> episodes = (List<Map<String, Object>>) movie.get("episodes");
                    if (episodes != null) {
                        episodeList.clear();
                        for (Map<String, Object> ep : episodes) {
                            String epTitle = (String) ep.get("title");
                            String epSlug = (String) ep.get("slug");
                            // Lấy URL video từ server_data hoặc trường tương ứng
                            String videoUrl = "";
                            Object serverData = ep.get("server_data");
                            if (serverData instanceof String) {
                                videoUrl = (String) serverData;
                            }
                            // Nếu server_data phức tạp, cần parse thêm

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
