package com.hidaymovie.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hidaymovie.BuildConfig;
import com.hidaymovie.R;
import com.hidaymovie.adapter.CastAdapter;
import com.hidaymovie.adapter.MovieAdapter;
import com.hidaymovie.adapter.ReviewAdapter;
import com.hidaymovie.model.Cast;
import com.hidaymovie.model.CreditResponse;
import com.hidaymovie.model.Genre;
import com.hidaymovie.model.Movie;
import com.hidaymovie.model.MovieResponse;
import com.hidaymovie.model.Review;
import com.hidaymovie.network.MovieApiService;
import com.hidaymovie.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";
    private String movieId;
    private MovieApiService apiService;

    // Views
    private CoordinatorLayout mainContent;
    private FrameLayout fullscreenContainer;
    private ImageView movieBackdrop, playButtonOverlay;
    private WebView moviePlayerWebView;
    private TextView movieTitle, movieReleaseYear, movieRuntime, movieRating, movieOverview;
    private ChipGroup genreChipGroup;
    private Button writeReviewButton;
    private ImageButton favoriteButton;
    private TextView textViewShowMore;

    // Fullscreen video handling
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    // RecyclerViews and Adapters
    private RecyclerView castRecyclerView, similarMoviesRecyclerView, reviewsRecyclerView;
    private CastAdapter castAdapter;
    private MovieAdapter similarMoviesAdapter;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private boolean isFavorite = false;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieId = getIntent().getStringExtra("movie_id");
        if (movieId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = RetrofitClient.getMovieApiService();
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initViews();
        setupToolbar();
        loadAllData();

        favoriteButton.setOnClickListener(v -> toggleFavoriteStatus());

        playButtonOverlay.setOnClickListener(v -> {
            saveToHistory();
            playMovieInWebView();
        });

        writeReviewButton.setOnClickListener(v -> showSubmitReviewDialog());
    }

    private void initViews() {
        mainContent = findViewById(R.id.main_content);
        fullscreenContainer = findViewById(R.id.fullscreen_container);
        movieBackdrop = findViewById(R.id.movie_backdrop);
        playButtonOverlay = findViewById(R.id.play_button_overlay);
        moviePlayerWebView = findViewById(R.id.movie_player_webview);
        movieTitle = findViewById(R.id.movie_title);
        movieReleaseYear = findViewById(R.id.movie_release_year);
        movieRuntime = findViewById(R.id.movie_runtime);
        movieRating = findViewById(R.id.movie_rating);
        movieOverview = findViewById(R.id.movie_overview);
        genreChipGroup = findViewById(R.id.genre_chip_group);
        writeReviewButton = findViewById(R.id.write_review_button);
        favoriteButton = findViewById(R.id.favorite_button);
        textViewShowMore = findViewById(R.id.text_view_show_more);

        castRecyclerView = findViewById(R.id.cast_recycler_view);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        similarMoviesRecyclerView = findViewById(R.id.similar_movies_recycler_view);
        similarMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setNestedScrollingEnabled(false);

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void playMovieInWebView() {
        movieBackdrop.setVisibility(View.GONE);
        playButtonOverlay.setVisibility(View.GONE);
        moviePlayerWebView.setVisibility(View.VISIBLE);

        String videoUrl = "https://vidsrc.to/embed/movie/" + movieId;

        WebSettings webSettings = moviePlayerWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        moviePlayerWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (customView != null) {
                    onHideCustomView();
                    return;
                }
                customView = view;
                customViewCallback = callback;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullscreenContainer.addView(customView);
                fullscreenContainer.setVisibility(View.VISIBLE);
                mainContent.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (customView == null) {
                    return;
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullscreenContainer.removeView(customView);
                fullscreenContainer.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
                customView = null;
                customViewCallback.onCustomViewHidden();
            }
        });

        moviePlayerWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String clickedDomain = Uri.parse(url).getHost();
                if (clickedDomain != null && !clickedDomain.contains("vidsrc.to")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        moviePlayerWebView.loadUrl(videoUrl);
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            moviePlayerWebView.getWebChromeClient().onHideCustomView();
        } else if (moviePlayerWebView.getVisibility() == View.VISIBLE) {
            moviePlayerWebView.loadUrl("about:blank");
            moviePlayerWebView.setVisibility(View.GONE);
            movieBackdrop.setVisibility(View.VISIBLE);
            playButtonOverlay.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        moviePlayerWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviePlayerWebView.onResume();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void loadAllData() {
        String apiKey = BuildConfig.TMDB_API_KEY;
        loadMovieDetails(apiKey);
        loadMovieCredits(apiKey);
        loadSimilarMovies(apiKey);
        loadReviewsFromFirestore();
        if (currentUser != null) {
            checkIfFavorite();
        }
    }

    private void showSubmitReviewDialog() {
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_submit_review, null);
        builder.setView(dialogView);
        final RatingBar ratingBar = dialogView.findViewById(R.id.submit_rating_bar);
        final EditText commentEditText = dialogView.findViewById(R.id.edit_text_comment);
        builder.setPositiveButton("Gửi", (dialog, which) -> {
            float rating = ratingBar.getRating();
            String comment = commentEditText.getText().toString().trim();
            if (rating > 0) {
                submitReviewToFirestore(rating, comment);
            } else {
                Toast.makeText(this, "Vui lòng cho điểm sao", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submitReviewToFirestore(float rating, String comment) {
        String userId = currentUser.getUid();
        String userName = currentUser.getDisplayName();
        String avatarUrl = (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : "";
        Review review = new Review(userName, comment, rating, userId, userName, avatarUrl);
        db.collection("movies").document(movieId).collection("reviews").document(userId)
                .set(review)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Gửi đánh giá thành công", Toast.LENGTH_SHORT).show();
                    loadReviewsFromFirestore();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadReviewsFromFirestore() {
        db.collection("movies").document(movieId).collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviewList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            reviewList.add(review);
                        }
                        reviewAdapter.notifyDataSetChanged();
                        updateRatingFromReviews(reviewList);
                    } else {
                        Log.w(TAG, "Lỗi khi tải review từ Firestore.", task.getException());
                    }
                });
    }

    private void updateRatingFromReviews(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            movieRating.setText("Chưa có đánh giá");
            return;
        }

        float totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        float averageRating = totalRating / reviews.size();
        movieRating.setText(String.format("★ %.1f (%d đánh giá)", averageRating, reviews.size()));
    }


    private void loadMovieDetails(String apiKey) {
        apiService.getMovieDetails(movieId, apiKey, "vi-VN", "videos").enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentMovie = response.body();
                    displayMovieDetails(currentMovie);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) { Log.e(TAG, "Lỗi tải chi tiết phim", t); }
        });
    }

    private void displayMovieDetails(Movie movie) {
        String backdropUrl = "https://image.tmdb.org/t/p/w780" + movie.getBackdropPath();
        Glide.with(this).load(backdropUrl).into(movieBackdrop);
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());

        setupShowMoreButton();

        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            movieReleaseYear.setText(movie.getReleaseDate().substring(0, 4));
        }
        movieRuntime.setText(movie.getRuntime() + " phút");
        movieRating.setText("★ Đang tải...");

        genreChipGroup.removeAllViews();
        if (movie.getGenres() != null) {
            for (Genre genre : movie.getGenres()) {
                Chip chip = new Chip(this);
                chip.setText(genre.getName());
                genreChipGroup.addView(chip);
            }
        }
    }

    private void setupShowMoreButton() {
        movieOverview.post(() -> {
            Layout layout = movieOverview.getLayout();
            if (layout != null) {
                int lines = layout.getLineCount();
                if (lines > 0) {
                    if (layout.getEllipsisCount(lines - 1) > 0) {
                        textViewShowMore.setVisibility(View.VISIBLE);
                        textViewShowMore.setOnClickListener(new View.OnClickListener() {
                            private boolean isExpanded = false;
                            @Override
                            public void onClick(View v) {
                                if (isExpanded) {
                                    movieOverview.setMaxLines(4);
                                    textViewShowMore.setText("Xem thêm");
                                } else {
                                    movieOverview.setMaxLines(Integer.MAX_VALUE);
                                    textViewShowMore.setText("Thu gọn");
                                }
                                isExpanded = !isExpanded;
                            }
                        });
                    } else {
                        textViewShowMore.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    private void loadMovieCredits(String apiKey) {
        apiService.getMovieCredits(movieId, apiKey).enqueue(new Callback<CreditResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreditResponse> call, @NonNull Response<CreditResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cast> castList = response.body().getCast();
                    castAdapter = new CastAdapter(MovieDetailActivity.this, castList);
                    castRecyclerView.setAdapter(castAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<CreditResponse> call, @NonNull Throwable t) { Log.e(TAG, "Lỗi tải diễn viên", t); }
        });
    }

    private void loadSimilarMovies(String apiKey) {
        apiService.getSimilarMovies(movieId, apiKey, "vi-VN", 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    similarMoviesAdapter = new MovieAdapter(MovieDetailActivity.this);
                    similarMoviesAdapter.setMovies(response.body().getResults());
                    similarMoviesRecyclerView.setAdapter(similarMoviesAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) { Log.e(TAG, "Lỗi tải phim tương tự", t); }
        });
    }

    private void saveToHistory() {
        if (currentUser == null || movieId == null || currentMovie == null) return;
        DocumentReference historyRef = db.collection("users").document(currentUser.getUid()).collection("history").document(movieId);
        Map<String, Object> historyData = new HashMap<>();
        historyData.put("id", currentMovie.getId());
        historyData.put("title", currentMovie.getTitle());
        historyData.put("posterPath", currentMovie.getPosterPath());
        historyData.put("watchedAt", FieldValue.serverTimestamp());
        historyRef.set(historyData).addOnSuccessListener(aVoid -> Log.d(TAG, "Đã lưu vào lịch sử")).addOnFailureListener(e -> Log.w(TAG, "Lỗi lưu lịch sử", e));
    }

    private void checkIfFavorite() {
        if (currentUser == null) return;
        DocumentReference movieRef = db.collection("users").document(currentUser.getUid()).collection("favorites").document(movieId);
        movieRef.get().addOnSuccessListener(documentSnapshot -> {
            isFavorite = documentSnapshot.exists();
            updateFavoriteButtonUI();
        });
    }

    private void toggleFavoriteStatus() {
        if (currentUser == null || currentMovie == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference movieRef = db.collection("users").document(currentUser.getUid()).collection("favorites").document(movieId);

        if (isFavorite) {
            // Xóa khỏi yêu thích
            movieRef.delete().addOnSuccessListener(aVoid -> {
                isFavorite = false;
                updateFavoriteButtonUI();
                Toast.makeText(this, "Đã xóa khỏi Yêu thích", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Thêm vào yêu thích - LƯU TRỰC TIẾP ĐỐI TƯỢNG MOVIE
            movieRef.set(currentMovie).addOnSuccessListener(aVoid -> {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}