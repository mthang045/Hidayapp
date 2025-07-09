package com.hidaymovie.main;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.adapter.ReviewAdapter;
import com.hidaymovie.R;
import com.hidaymovie.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private EditText editReview;
    private Button btnSubmitReview;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Ánh xạ các view
        editReview = findViewById(R.id.editReview);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        // Thiết lập RecyclerView
        reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(reviewAdapter);

        // Khi người dùng nhấn nút gửi review
        btnSubmitReview.setOnClickListener(v -> {
            String reviewText = editReview.getText().toString().trim();
            if (!reviewText.isEmpty()) {
                // === SỬA LỖI ===
                // Dòng code dưới đây gây lỗi vì constructor của class Review đã thay đổi.
                // Review newReview = new Review("User", reviewText);
                // reviewList.add(newReview);
                // reviewAdapter.notifyDataSetChanged();

                // Chức năng review đầy đủ (gồm vote sao và lưu vào Firebase)
                // hiện đã được tích hợp hoàn toàn vào màn hình MovieDetailActivity.
                // Vì vậy, chức năng cũ ở đây được vô hiệu hóa để khắc phục lỗi build.

                editReview.setText("");  // Làm sạch EditText
            }
        });
    }
}
