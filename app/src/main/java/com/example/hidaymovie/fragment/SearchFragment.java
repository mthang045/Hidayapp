package com.example.hidaymovie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.example.hidaymovie.adapter.MovieAdapter;
import com.example.hidaymovie.network.MovieApiService;
import com.example.hidaymovie.network.MovieResponse;
import com.example.hidaymovie.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private MovieAdapter searchAdapter;

    private static final String API_KEY = "d457a3503903f9582d34200aeddebdf5";
    private static final String LANGUAGE = "en-US";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các views
        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);

        // === THAY ĐỔI NẰM Ở ĐÂY ===
        // Sử dụng GridLayoutManager với 2 cột
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchAdapter = new MovieAdapter(getContext());
        searchResultsRecyclerView.setAdapter(searchAdapter);

        // Xử lý sự kiện khi người dùng nhấn nút tìm kiếm trên bàn phím
        setupSearchHandler();
    }

    private void setupSearchHandler() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                    hideKeyboard();
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        Toast.makeText(getContext(), "Đang tìm kiếm: " + query, Toast.LENGTH_SHORT).show();

        MovieApiService apiService = RetrofitClient.getMovieApiService();

        apiService.searchMovies(API_KEY, query, LANGUAGE, 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    if (response.body().getResults().isEmpty()) {
                        Toast.makeText(getContext(), "Không tìm thấy kết quả nào", Toast.LENGTH_SHORT).show();
                    }
                    searchAdapter.setMovies(response.body().getResults());
                } else {
                    if(isAdded()) Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if(isAdded()) {
                    Log.e("API_ERROR", "Search: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
