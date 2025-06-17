package com.example.hidaymovie.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hidaymovie.R;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieTitle, movieDescription, movieReleaseDate, movieRating;
    private Button viewReviewButton, watchMovieButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Khởi tạo các view
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieRating = findViewById(R.id.movieRating);
        viewReviewButton = findViewById(R.id.viewReviewButton);
        watchMovieButton = findViewById(R.id.watchMovieButton);

        // Lấy dữ liệu từ Intent
        String posterUrl = getIntent().getStringExtra("poster_url");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String releaseDate = getIntent().getStringExtra("release_date");
        String rating = getIntent().getStringExtra("rating");

        // Cập nhật các trường thông tin
        Glide.with(this)
                .load(posterUrl)
                .into(moviePoster);

        movieTitle.setText(title);
        movieDescription.setText(description);
        movieReleaseDate.setText("Release Date: " + releaseDate);
        movieRating.setText("Rating: " + rating);

        // Xử lý nút "Xem Review"
        viewReviewButton.setOnClickListener(v -> {
            // Chuyển đến màn hình review (hoặc một trang web review)
            Intent reviewIntent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
            reviewIntent.putExtra("movie_title", title);  // Truyền tiêu đề phim nếu cần
            startActivity(reviewIntent);
        });

        // Xử lý nút "Xem Phim" (chúng ta sẽ làm sau)
        watchMovieButton.setOnClickListener(v -> {
            // Chuyển đến màn hình phát video (VideoPlayerActivity)
            String videoUrl = getIntent().getStringExtra("video_url");  // Giả sử bạn truyền URL video
            Intent videoIntent = new Intent(MovieDetailActivity.this, VideoPlayerActivity.class);
            videoIntent.putExtra("video_url", videoUrl);
            startActivity(videoIntent);
        });
    }
}
