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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_REGISTER = 1001;
    private static MediaPlayer mediaPlayer;

    ImageButton btnVolume;
    Button btnStart;
    boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thiết lập layout theo hướng màn hình
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        } else {
            setContentView(R.layout.activity_start);
        }

        // Nhạc nền
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.intro);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Log.d("AUDIO", "MediaPlayer started: " + mediaPlayer.isPlaying());
            Toast.makeText(this, "🔊 Âm thanh: Đang phát", Toast.LENGTH_SHORT).show();
        }

        // Nút Volume
        btnVolume = findViewById(R.id.btnVolume);
        btnVolume.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Toast.makeText(this, "🔇 Âm thanh đã tắt", Toast.LENGTH_SHORT).show();
                } else {
                    mediaPlayer.start();
                    Toast.makeText(this, "🔊 Âm thanh đã bật", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút Start (ban đầu bị vô hiệu)
        btnStart = findViewById(R.id.btnStart);
        btnStart.setEnabled(false);
        btnStart.setAlpha(0.5f);
        btnStart.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            startActivity(new Intent(this, RacingActivity.class));
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
            setRequestedOrientation(
                    orientation == Configuration.ORIENTATION_PORTRAIT ?
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            );
        });

        // Nút Đăng ký
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            btnStart.setEnabled(true);
            btnStart.setAlpha(1.0f);
            Toast.makeText(this, "✅ Đăng ký thành công! Bạn có thể bắt đầu chơi.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Không release mediaPlayer tại đây nếu muốn nhạc chạy xuyên suốt khi xoay màn hình
    }
}
