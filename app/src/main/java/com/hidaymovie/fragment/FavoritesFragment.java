package com.hidaymovie.fragment;
import com.hidaymovie.adapter.FavoriteMovieAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hidaymovie.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private FavoriteMovieAdapter favoritesAdapter;
    private List<Movie> favoriteMovieList;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoriteMovieList = new ArrayList<>();
        favoritesAdapter = new FavoriteMovieAdapter(getContext());
        favoritesAdapter.setMovies(favoriteMovieList);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        loadFavorites();
    }

    private void loadFavorites() {
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).collection("favorites")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.w("FavoritesFragment", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            favoriteMovieList.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Movie movie = new Movie();

                                // Đọc đầy đủ thông tin phim từ Firestore
                                if (doc.getLong("id") != null) {
                                    movie.setId(doc.getLong("id").intValue());
                                }
                                movie.setTitle(doc.getString("title"));
                                movie.setPosterPath(doc.getString("poster_path"));

                                // === ĐỌC THÊM DỮ LIỆU ĐỂ HIỂN THỊ ĐẦY ĐỦ ===
                                movie.setOverview(doc.getString("overview"));
                                movie.setReleaseDate(doc.getString("release_date"));
                                if (doc.getDouble("vote_average") != null) {
                                    movie.setVoteAverage(doc.getDouble("vote_average").floatValue());
                                }

                                favoriteMovieList.add(movie);
                            }
                            favoritesAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
