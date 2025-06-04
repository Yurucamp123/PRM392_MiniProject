package com.example.miniproject_carracing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static MediaPlayer mediaPlayer; // static để giữ lại khi xoay màn hình
    ImageButton btnVolume;
    boolean isPlaying = true; // mặc định bật

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dùng layout tương ứng với orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        } else {
            setContentView(R.layout.activity_start);
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.intro);

        mediaPlayer = MediaPlayer.create(this, R.raw.intro);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Log.d("AUDIO", "MediaPlayer started: " + mediaPlayer.isPlaying());
            Toast.makeText(this, "🔊 Âm thanh: Đang phát", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Không thể phát nhạc!", Toast.LENGTH_LONG).show();
        }


        // Nút âm lượng
        btnVolume = findViewById(R.id.btnVolume);
        btnVolume.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Toast.makeText(this, "🔇 Âm thanh đã tắt", Toast.LENGTH_SHORT).show();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    Toast.makeText(this, "🔊 Âm thanh đã bật", Toast.LENGTH_SHORT).show();
                    isPlaying = true;
                }
            }
        });

        // Nút Start
        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null; // quan trọng!
            }
            startActivity(new Intent(this, IntroActivity.class));
        });

        // Nút Exit
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            Toast.makeText(this, "Thoát game", Toast.LENGTH_SHORT).show();
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            finishAffinity();
        });

        // Nút Guide
        Button btnGuide = findViewById(R.id.btnGuide);
        if (btnGuide != null) {
            btnGuide.setOnClickListener(v -> Toast.makeText(this, "Hướng dẫn đang được cập nhật!", Toast.LENGTH_SHORT).show());
        }

        // Nút Rotate
        Button btnRotate = findViewById(R.id.btnRotate);
        btnRotate.setOnClickListener(v -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Toast.makeText(this, "Xoay ngang", Toast.LENGTH_SHORT).show();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Toast.makeText(this, "Xoay dọc", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Không release ở đây nếu muốn giữ nhạc chạy khi xoay
    }
}
