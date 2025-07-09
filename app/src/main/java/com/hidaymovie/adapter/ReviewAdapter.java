package com.hidaymovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hidaymovie.R;
import com.hidaymovie.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    public void setReviews(List<Review> reviews) {
        this.reviewList = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // Ưu tiên hiển thị tên người dùng từ Firebase, nếu không có thì mới dùng tên từ TMDB
        String displayName = (review.getUserName() != null && !review.getUserName().isEmpty())
                ? review.getUserName()
                : review.getAuthor();
        holder.userName.setText(displayName);
        holder.reviewContent.setText(review.getContent());
        holder.ratingBar.setRating(review.getRating());

        // Tải ảnh đại diện
        Glide.with(context)
                .load(review.getUserAvatarUrl())
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_profile) // Ảnh mặc định
                .error(R.drawable.ic_profile) // Ảnh lỗi
                .into(holder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        TextView reviewContent;
        RatingBar ratingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            reviewContent = itemView.findViewById(R.id.review_content);
            ratingBar = itemView.findViewById(R.id.rating_bar_indicator);
        }
    }
}
