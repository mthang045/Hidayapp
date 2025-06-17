package com.example.hidaymovie.main;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.hidaymovie.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Khởi tạo player
        player = new ExoPlayer.Builder(this).build();

        // Lấy video URL từ Intent
        String videoUrl = getIntent().getStringExtra("video_url");

        // Kiểm tra nếu videoUrl hợp lệ
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // Tạo MediaItem
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(videoUrl)
                    .build();

            // Gán MediaItem cho player
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        } else {
            // Xử lý khi không có video URL
            Toast.makeText(this, "Video URL không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Giải phóng tài nguyên player khi không sử dụng nữa
        player.release();
    }
}
