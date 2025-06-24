package com.example.hidaymovie.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hidaymovie.R;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieTitle, movieDescription, movieReleaseDate, movieRating;
    private Button viewReviewButton, watchMovieButton;
    private ImageButton favoriteButton;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private boolean isFavorite = false;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Ánh xạ các views
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieRating = findViewById(R.id.movieRating);
        viewReviewButton = findViewById(R.id.viewReviewButton);
        watchMovieButton = findViewById(R.id.watchMovieButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movie_id");
        String posterUrl = intent.getStringExtra("poster_url");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String releaseDate = intent.getStringExtra("release_date");
        String rating = intent.getStringExtra("rating");

        // Hiển thị dữ liệu, kiểm tra null để tránh lỗi
        movieTitle.setText(title != null ? title : "Không có tiêu đề");
        movieDescription.setText(description != null ? description : "Không có mô tả.");
        movieReleaseDate.setText("Ngày phát hành: " + (releaseDate != null ? releaseDate : "N/A"));
        movieRating.setText("Đánh giá: " + (rating != null ? rating : "N/A"));

        Glide.with(this).load(posterUrl).placeholder(R.drawable.ic_placeholder).error(R.drawable.error_image).into(moviePoster);

        // Kiểm tra trạng thái yêu thích
        if (currentUser != null && movieId != null) {
            checkIfFavorite();
        }

        // Xử lý sự kiện nhấn nút yêu thích
        favoriteButton.setOnClickListener(v -> {
            if (currentUser != null && movieId != null) {
                toggleFavoriteStatus();
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nhấn nút xem phim
        watchMovieButton.setOnClickListener(v -> {
            Intent playerIntent = new Intent(MovieDetailActivity.this, MoviePlayerActivity.class);
            playerIntent.putExtra("movie_id", movieId);
            startActivity(playerIntent);
        });

        // Xử lý sự kiện nhấn nút xem review
        viewReviewButton.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
            reviewIntent.putExtra("movie_id", movieId);
            startActivity(reviewIntent);
        });
    }

    // Hàm kiểm tra xem phim đã có trong danh sách yêu thích chưa
    private void checkIfFavorite() {
        DocumentReference movieRef = db.collection("users").document(currentUser.getUid())
                .collection("favorites").document(movieId);

        movieRef.get().addOnSuccessListener(documentSnapshot -> {
            isFavorite = documentSnapshot.exists();
            updateFavoriteButtonUI();
        });
    }

    // Hàm thêm hoặc xóa phim khỏi danh sách yêu thích
    private void toggleFavoriteStatus() {
        DocumentReference movieRef = db.collection("users").document(currentUser.getUid())
                .collection("favorites").document(movieId);

        if (isFavorite) {
            // Nếu đang là yêu thích -> Xóa
            movieRef.delete().addOnSuccessListener(aVoid -> {
                isFavorite = false;
                updateFavoriteButtonUI();
                Toast.makeText(MovieDetailActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Nếu chưa phải yêu thích -> Thêm
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String releaseDate = intent.getStringExtra("release_date");
            String rating = intent.getStringExtra("rating");
            String posterUrl = intent.getStringExtra("poster_url");
            String rawPosterPath = (posterUrl != null) ? posterUrl.replace("https://image.tmdb.org/t/p/w500", "") : "";

            Map<String, Object> movieData = new HashMap<>();
            try {
                movieData.put("id", Integer.parseInt(movieId));
                movieData.put("title", title);
                movieData.put("poster_path", rawPosterPath);
                movieData.put("overview", description);
                movieData.put("release_date", releaseDate);
                movieData.put("vote_average", Float.parseFloat(rating));
            } catch (NumberFormatException | NullPointerException e) {
                Toast.makeText(this, "Lỗi khi lưu dữ liệu phim", Toast.LENGTH_SHORT).show();
                return;
            }

            movieRef.set(movieData).addOnSuccessListener(aVoid -> {
                isFavorite = true;
                updateFavoriteButtonUI();
                Toast.makeText(MovieDetailActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Hàm cập nhật giao diện của nút yêu thích
    private void updateFavoriteButtonUI() {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}
