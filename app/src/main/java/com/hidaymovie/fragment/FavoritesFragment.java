package com.hidaymovie.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hidaymovie.R;
import com.hidaymovie.adapter.FavoriteMovieAdapter;
import com.hidaymovie.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private FavoriteMovieAdapter favoritesAdapter;
    private ProgressBar favoritesProgressBar;
    private TextView emptyFavoritesTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoritesProgressBar = view.findViewById(R.id.favoritesProgressBar);
        emptyFavoritesTextView = view.findViewById(R.id.emptyFavoritesTextView);

        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại danh sách yêu thích mỗi khi người dùng quay lại màn hình này
        loadFavoriteMovies();
    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoriteMovieAdapter(getContext());
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoritesRecyclerView.setAdapter(favoritesAdapter);
    }

    private void loadFavoriteMovies() {
        if (mAuth.getCurrentUser() == null) {
            emptyFavoritesTextView.setVisibility(View.VISIBLE);
            favoritesProgressBar.setVisibility(View.GONE);
            return;
        }

        // Hiển thị trạng thái chờ
        favoritesProgressBar.setVisibility(View.VISIBLE);
        favoritesRecyclerView.setVisibility(View.GONE);
        emptyFavoritesTextView.setVisibility(View.GONE);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;

                    favoritesProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        List<Movie> favoriteMovies = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            favoriteMovies.add(movie);
                        }

                        if (favoriteMovies.isEmpty()) {
                            // Danh sách trống -> Hiển thị thông báo
                            emptyFavoritesTextView.setVisibility(View.VISIBLE);
                        } else {
                            // Có dữ liệu -> Hiển thị RecyclerView
                            favoritesRecyclerView.setVisibility(View.VISIBLE);
                            favoritesAdapter.setMovies(favoriteMovies);
                        }
                    } else {
                        Log.e("FIRESTORE_ERROR", "Error getting documents: ", task.getException());
                        Toast.makeText(getContext(), "Lỗi tải danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}