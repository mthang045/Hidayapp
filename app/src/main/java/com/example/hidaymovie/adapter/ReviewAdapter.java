package com.example.hidaymovie.main;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTime, txtContent;
        RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            txtUsername = view.findViewById(R.id.txtUsername);
            txtTime = view.findViewById(R.id.txtTime);
            txtContent = view.findViewById(R.id.txtContent);
            ratingBar = view.findViewById(R.id.ratingBarDisplay);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review r = reviewList.get(position);
        holder.txtUsername.setText(r.username);
        holder.txtContent.setText(r.content);
        holder.ratingBar.setRating(r.rating);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        holder.txtTime.setText(sdf.format(new Date(r.timestamp)));

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
