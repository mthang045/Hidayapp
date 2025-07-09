package com.hidaymovie.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hidaymovie.R;
import com.hidaymovie.adapter.MovieAdapter;
import com.hidaymovie.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private RecyclerView historyRecyclerView;
    private MovieAdapter historyAdapter;
    private List<Movie> historyMovieList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút Back
        }

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Dạng lưới 2 cột
        historyMovieList = new ArrayList<>();
        historyAdapter = new MovieAdapter(this);
        historyAdapter.setMovies(historyMovieList);
        historyRecyclerView.setAdapter(historyAdapter);

        loadWatchHistory();
    }

    private void loadWatchHistory() {
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).collection("history")
                    .orderBy("watchedAt", Query.Direction.DESCENDING)
                    .limit(50) // Tải tối đa 50 phim gần nhất
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            historyMovieList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Movie movie = new Movie();
                                if (document.getLong("id") != null) movie.setId(document.getLong("id").intValue());
                                movie.setTitle(document.getString("title"));
                                movie.setPosterPath(document.getString("poster_path"));
                                historyMovieList.add(movie);
                            }
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Lỗi khi tải lịch sử.", task.getException());
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Đóng Activity hiện tại và quay về
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
    