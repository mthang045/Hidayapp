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
import com.example.hidaymovie.main.VideoPlayerActivity;
import com.hidaymovie.R;
import com.example.hidaymovie.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;

    // Constructor để nhận context
    public MovieAdapter(Context context) {
        this.context = context;
    }

    // Set dữ liệu cho Adapter
    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();  // Cập nhật dữ liệu
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo item cho mỗi view trong RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);  // Gắn layout cho mỗi item
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Set tên phim và poster cho từng item
        holder.titleTextView.setText(movie.getTitle());

        // Kiểm tra posterPath trước khi tải ảnh
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

        if (movie.getPosterPath() == null || movie.getPosterPath().isEmpty()) {
            // Nếu không có posterPath, dùng ảnh mặc định
            posterUrl = "https://www.example.com/default_image.jpg"; // Thay thế bằng URL ảnh mặc định
        }

        // Dùng Glide để tải ảnh poster từ URL
        Glide.with(holder.posterImageView.getContext())
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder) // Hình ảnh thay thế khi chưa tải được poster
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.posterImageView);

        // Khi người dùng nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("video_url", movie.getVideoUrl()); // Truyền URL của video vào VideoPlayerActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng item trong RecyclerView
        return movieList == null ? 0 : movieList.size();
    }

    // ViewHolder cho mỗi item
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
