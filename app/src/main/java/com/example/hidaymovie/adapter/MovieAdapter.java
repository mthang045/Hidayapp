package com.example.hidaymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Set tên phim cho từng item
        holder.titleTextView.setText(movie.getTitle());

        // Kiểm tra posterPath trước khi tải ảnh
        String posterPath = movie.backdrop_path;
        Log.d("posterPath", posterPath != null ? posterPath : "không tồn tại");
        String posterUrl;

        // Nếu posterPath hợp lệ, nối vào URL đầy đủ, nếu không thì sử dụng hình ảnh mặc định
        if (posterPath == null || posterPath.isEmpty()) {
            posterUrl = "https://image.tmdb.org/t/p/w500/default_image.jpg";  // Hình ảnh mặc định khi không có poster
        } else {
            posterUrl = "https://image.tmdb.org/t/p/w500/" + posterPath;  // Nối URL cơ sở với poster_path
        }

        // Dùng Glide để tải ảnh poster từ URL
        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)  // Hình ảnh thay thế khi chưa tải được poster
                .error(R.drawable.error_image)  // Hình ảnh khi có lỗi
                .into(holder.posterImageView);

        // Khi người dùng nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            // Truyền thông tin chi tiết của movie vào Intent
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("poster_url", posterUrl);  // Truyền poster URL đầy đủ
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("description", movie.getOverview());
            intent.putExtra("release_date", movie.getReleaseDate());
            intent.putExtra("rating", movie.getVoteAverage());
            intent.putExtra("video_url", movie.getVideoUrl()); // Truyền video URL (nếu có)
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
