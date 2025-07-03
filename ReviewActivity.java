package com.example.hidaymovie.main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.google.firebase.database.*;

import java.util.*;

public class ReviewActivity extends AppCompatActivity {

    private EditText editUsername, editContent;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private RecyclerView reviewRecyclerView;

    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter adapter;

    private DatabaseReference reviewRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        editUsername = findViewById(R.id.editUsername);
        editContent = findViewById(R.id.editContent);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        reviewRef = FirebaseDatabase.getInstance().getReference("reviews");

        adapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(adapter);

        btnSubmit.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String content = editContent.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
            String formattedTime = sdf.format(new Date());
            Review review = new Review(username, content, rating, formattedTime);
            reviewRef.push().setValue(review);

            editUsername.setText("");
            editContent.setText("");
            ratingBar.setRating(0);
        });

        loadReviews();
    }

    private void loadReviews() {
        reviewRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Review review = snap.getValue(Review.class);
                    if (review != null) {
                        reviewList.add(review);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
