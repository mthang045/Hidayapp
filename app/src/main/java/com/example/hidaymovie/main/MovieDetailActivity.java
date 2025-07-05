package com.example.hidaymovie.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hidaymovie.R;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";

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

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Ánh xạ các view và lấy dữ liệu từ Intent như cũ
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieRating = findViewById(R.id.movieRating);
        viewReviewButton = findViewById(R.id.viewReviewButton);
        watchMovieButton = findViewById(R.id.watchMovieButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("movie_id");
        String posterUrl = intent.getStringExtra("poster_url");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String releaseDate = intent.getStringExtra("release_date");
        String rating = intent.getStringExtra("rating");

        movieTitle.setText(title != null ? title : "Không có tiêu đề");
        movieDescription.setText(description != null ? description : "Không có mô tả.");
        movieReleaseDate.setText("Ngày phát hành: " + (releaseDate != null ? releaseDate : "N/A"));
        movieRating.setText("Đánh giá: " + (rating != null ? rating : "N/A"));

        Glide.with(this).load(posterUrl).placeholder(R.drawable.ic_placeholder).error(R.drawable.error_image).into(moviePoster);

        if (currentUser != null && movieId != null) {
            checkIfFavorite();
        }

        favoriteButton.setOnClickListener(v -> toggleFavoriteStatus());

        // === THAY ĐỔI NẰM Ở ĐÂY ===
        watchMovieButton.setOnClickListener(v -> {
            // 1. Lưu phim vào lịch sử
            saveToHistory();

            // 2. Mở trình phát phim
            Intent playerIntent = new Intent(MovieDetailActivity.this, MoviePlayerActivity.class);
            playerIntent.putExtra("movie_id", movieId);
            startActivity(playerIntent);
        });

        viewReviewButton.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
            reviewIntent.putExtra("movie_id", movieId);
            startActivity(reviewIntent);
        });
    }

    // === HÀM MỚI ĐỂ LƯU LỊCH SỬ ===
    private void saveToHistory() {
        if (currentUser == null || movieId == null) {
            return; // Không lưu nếu người dùng chưa đăng nhập hoặc không có ID phim
        }

        // Tạo một document mới trong collection "history"
        DocumentReference historyRef = db.collection("users").document(currentUser.getUid())
                .collection("history").document(movieId);

        // Lấy thông tin phim từ Intent để lưu lại
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String posterUrl = intent.getStringExtra("poster_url");
        String rawPosterPath = (posterUrl != null) ? posterUrl.replace("https://image.tmdb.org/t/p/w500", "") : "";

        Map<String, Object> historyData = new HashMap<>();
        historyData.put("id", Integer.parseInt(movieId));
        historyData.put("title", title);
        historyData.put("poster_path", rawPosterPath);
        // Thêm một trường timestamp để biết thời gian xem
        historyData.put("watchedAt", FieldValue.serverTimestamp());

        // Dùng .set() để ghi đè, đảm bảo mỗi phim chỉ có 1 bản ghi lịch sử
        // và được cập nhật thời gian xem mới nhất
        historyRef.set(historyData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Đã lưu vào lịch sử thành công"))
                .addOnFailureListener(e -> Log.w(TAG, "Lỗi khi lưu lịch sử", e));
    }

    // Các hàm xử lý yêu thích giữ nguyên như cũ
    private void checkIfFavorite() {
        if (currentUser == null || movieId == null) return;
        DocumentReference movieRef = db.collection("users").document(currentUser.getUid())
                .collection("favorites").document(movieId);

        movieRef.get().addOnSuccessListener(documentSnapshot -> {
            isFavorite = documentSnapshot.exists();
            updateFavoriteButtonUI();
        }).addOnFailureListener(e -> Log.w(TAG, "Lỗi kiểm tra Yêu thích", e));
    }

    private void toggleFavoriteStatus() {
        if (currentUser == null || movieId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để dùng tính năng này", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference movieRef = db.collection("users").document(currentUser.getUid())
                .collection("favorites").document(movieId);

        if (isFavorite) {
            movieRef.delete().addOnSuccessListener(aVoid -> {
                isFavorite = false;
                updateFavoriteButtonUI();
                Toast.makeText(this, "Đã xóa khỏi Yêu thích", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Lấy toàn bộ dữ liệu từ Intent để lưu
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String posterUrl = intent.getStringExtra("poster_url");
            String rawPosterPath = (posterUrl != null) ? posterUrl.replace("https://image.tmdb.org/t/p/w500", "") : "";

            Map<String, Object> movieData = new HashMap<>();
            movieData.put("id", Integer.parseInt(movieId));
            movieData.put("title", title);
            movieData.put("poster_path", rawPosterPath);
            movieData.put("overview", intent.getStringExtra("description"));
            movieData.put("release_date", intent.getStringExtra("release_date"));
            try {
                movieData.put("vote_average", Float.parseFloat(intent.getStringExtra("rating")));
            } catch (Exception e) {
                movieData.put("vote_average", 0f);
            }

            movieRef.set(movieData).addOnSuccessListener(aVoid -> {
                isFavorite = true;
                updateFavoriteButtonUI();
                Toast.makeText(this, "Đã thêm vào Yêu thích", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updateFavoriteButtonUI() {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}
