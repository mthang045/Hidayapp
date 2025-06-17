package com.example.hidaymovie.main;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidaymovie.adapter.ReviewAdapter;
import com.hidaymovie.R;
import com.example.hidaymovie.model.Review;

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
                // Tạo một đối tượng Review mới và thêm vào danh sách
                Review newReview = new Review("User", reviewText);  // "User" là tên người dùng
                reviewList.add(newReview);
                reviewAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
                editReview.setText("");  // Làm sạch EditText
            }
        });
    }
}
