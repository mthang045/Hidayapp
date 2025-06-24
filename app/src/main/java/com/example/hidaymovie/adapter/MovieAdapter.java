package com.example.hidaymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hidaymovie.main.MovieDetailActivity;
import com.hidaymovie.R;
import com.example.hidaymovie.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movieList == null || movieList.isEmpty()) return;

        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());

        // Ưu tiên dùng poster_path (ảnh đứng), nếu không có thì dùng backdrop_path
        String imagePath = movie.getPosterPath();
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = movie.getBackdropPath();
        }

        final String posterUrl;
        if (imagePath == null || imagePath.isEmpty()) {
            // Nếu cả hai đều không có thì dùng ảnh mặc định
            Glide.with(context)
                    .load(R.drawable.ic_placeholder) // Bạn cần có ảnh này trong drawable
                    .into(holder.posterImageView);
            posterUrl = "";
        } else {
            posterUrl = "https://image.tmdb.org/t/p/w500" + imagePath;
            Glide.with(context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.error_image) // Bạn cần có ảnh này
                    .into(holder.posterImageView);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);

            // Truyền các thông tin cần thiết sang màn hình chi tiết
            intent.putExtra("movie_id", String.valueOf(movie.getId()));
            intent.putExtra("poster_url", posterUrl);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("description", movie.getOverview());
            intent.putExtra("release_date", movie.getReleaseDate());
            intent.putExtra("rating", String.valueOf(movie.getVoteAverage()));

            // Dòng gây lỗi đã được xóa ở đây.
            // Chúng ta không cần truyền video_url nữa vì đã có movie_id.

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            posterImageView = itemView.findViewById(R.id.moviePoster);
        }
    }
}