package com.hidaymovie.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.hidaymovie.adapter.MovieAdapter;
import com.hidaymovie.model.MovieResponse;
import com.hidaymovie.network.MovieApiService;
import com.hidaymovie.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private static final String API_KEY = "e9e9d8da18ae29fc430845952232787c";
    private static final String LANGUAGE = "en-US";

    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private MovieAdapter searchAdapter;
    private ProgressBar searchProgressBar;
    private TextView emptyStateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các View
        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        searchProgressBar = view.findViewById(R.id.searchProgressBar);
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView);

        // Cài đặt RecyclerView
        setupRecyclerView();

        // Cài đặt sự kiện cho ô tìm kiếm
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        searchAdapter = new MovieAdapter(getContext());
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchResultsRecyclerView.setAdapter(searchAdapter);
    }

    private void performSearch(String query) {
        // Hiển thị trạng thái chờ và ẩn các view khác
        searchProgressBar.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        emptyStateTextView.setVisibility(View.GONE);

        MovieApiService apiService = RetrofitClient.getMovieApiService();
        apiService.searchMovies(API_KEY, query, LANGUAGE, 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (!isAdded()) return;

                searchProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty()) {
                    // Có kết quả -> Hiển thị RecyclerView
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                    searchAdapter.setMovies(response.body().getResults());
                } else {
                    // Không có kết quả -> Hiển thị thông báo
                    emptyStateTextView.setText("Không tìm thấy kết quả cho '" + query + "'");
                    emptyStateTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                searchProgressBar.setVisibility(View.GONE);
                emptyStateTextView.setText("Lỗi mạng, vui lòng thử lại.");
                emptyStateTextView.setVisibility(View.VISIBLE);
                Log.e("API_ERROR", "Search: " + t.getMessage());
            }
        });
    }
}