package com.example.hidaymovie.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.hidaymovie.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);

        // Tạo ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();

        // Tạo MediaItem từ URL của video
        String videoUrl = "https://path/to/your/video.mp4"; // Thay thế bằng URL của video
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);

        // Thiết lập player với MediaItem
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

        // Gán ExoPlayer vào PlayerView
        playerView.setPlayer(exoPlayer);

        // Bắt đầu phát video
        exoPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên khi không sử dụng ExoPlayer nữa
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
