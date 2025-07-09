package com.hidaymovie.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hidaymovie.R;

public class MoviePlayerActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_player);

        WebView webView = findViewById(R.id.webView);

        String movieId = getIntent().getStringExtra("movie_id");

        if (movieId != null && !movieId.isEmpty()) {
            String videoUrl = "https://vidsrc.to/embed/movie/" + movieId;

            // Cấu hình WebView nâng cao
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            // === PHẦN XỬ LÝ QUẢNG CÁO NẰM Ở ĐÂY ===
            // Chúng ta tạo một WebViewClient tùy chỉnh
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // Lấy domain của link mà người dùng nhấn vào
                    String clickedDomain = Uri.parse(url).getHost();

                    // Nếu domain của link được nhấn KHÔNG PHẢI là "vidsrc.to"
                    // (Tức là một link quảng cáo)
                    if (clickedDomain != null && !clickedDomain.contains("vidsrc.to")) {
                        // Tạo một Intent để mở link này trong trình duyệt bên ngoài
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);

                        // Rất quan trọng: return true để báo cho WebView rằng
                        // chúng ta đã xử lý link này, và WebView không cần làm gì thêm.
                        return true;
                    }

                    // Nếu link này vẫn thuộc vidsrc.to (ví dụ: chuyển tập)
                    // thì để WebView tự xử lý.
                    return false;
                }
            });

            // Tải URL phim vào WebView
            webView.loadUrl(videoUrl);

        } else {
            Toast.makeText(this, "Lỗi: Không nhận được ID phim.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
