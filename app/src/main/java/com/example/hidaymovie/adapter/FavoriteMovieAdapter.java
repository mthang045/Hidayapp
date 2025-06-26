package com.example.hidaymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hidaymovie.main.MovieDetailActivity;
import com.example.hidaymovie.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hidaymovie.R;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_favorite, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movieList == null || movieList.isEmpty() || position >= movieList.size()) return;

        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());

        String imagePath = movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()
                ? movie.getPosterPath()
                : movie.getBackdropPath();

        final String posterUrl;
        if (imagePath == null || imagePath.isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.ic_placeholder)
                    .into(holder.posterImageView);
            posterUrl = "";
        } else {
            posterUrl = "https://image.tmdb.org/t/p/w500" + imagePath;
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.error_image)
                    .into(holder.posterImageView);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movie_id", String.valueOf(movie.getId()));
            intent.putExtra("poster_url", posterUrl);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("description", movie.getOverview());
            intent.putExtra("release_date", movie.getReleaseDate());
            intent.putExtra("rating", String.valueOf(movie.getVoteAverage()));
            context.startActivity(intent);
        });

        holder.favoriteButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("favorites")
                    .whereEqualTo("id", movie.getId())
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            doc.getReference().delete();
                        }
                        movieList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, movieList.size());
                        Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;
        ImageButton favoriteButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            posterImageView = itemView.findViewById(R.id.moviePoster);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
