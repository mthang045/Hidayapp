package com.hidaymovie.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hidaymovie.R;
import com.hidaymovie.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movieList;
    private OnMovieClickListener listener;

    // Interface để bắt sự kiện click phim
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvStatus.setText(movie.getStatus());

        Glide.with(holder.itemView.getContext())
                .load(movie.getThumb())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.imgThumb);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvTitle, tvStatus;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
