package com.hidaymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.hidaymovie.R;
import com.hidaymovie.main.MovieListActivity;
import com.hidaymovie.model.Genre;
import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<Genre> genreList;
    private Context context;
    private List<String> colors;

    public GenreAdapter(Context context) {
        this.context = context;
        this.genreList = new ArrayList<>();
        // Chuẩn bị một danh sách màu sắc để dùng luân phiên
        colors = new ArrayList<>();
        colors.add("#673AB7"); // Tím
        colors.add("#E91E63"); // Hồng
        colors.add("#009688"); // Xanh mòng két
        colors.add("#FF5722"); // Cam
        colors.add("#3F51B5"); // Chàm
    }

    public void setGenres(List<Genre> genres) {
        this.genreList = genres;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.genre_item, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.genreName.setText(genre.getName());

        int colorIndex = position % colors.size();
        holder.cardView.setCardBackgroundColor(Color.parseColor(colors.get(colorIndex)));

        // Xử lý sự kiện khi người dùng nhấn vào một thể loại
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieListActivity.class);
            intent.putExtra("genre_id", genre.getId());
            intent.putExtra("genre_name", genre.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return genreList != null ? genreList.size() : 0;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreName;
        CardView cardView;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreName = itemView.findViewById(R.id.tv_genre_name);
            cardView = itemView.findViewById(R.id.card_view_genre);
        }
    }
}