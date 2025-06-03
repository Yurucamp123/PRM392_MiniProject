package com.example.miniproject_carracing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int clickSoundId;
    ImageButton btnVolume;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dùng layout tương ứng với orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land); // layout landscape tùy chỉnh
        } else {
            setContentView(R.layout.activity_main);
        }

        // Âm thanh click
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        clickSoundId = soundPool.load(this, R.raw.click_sound, 1);

        // Nút âm lượng
        btnVolume = findViewById(R.id.btnVolume);
        btnVolume.setOnClickListener(v -> {
            soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    Toast.makeText(this, "Âm thanh: Tạm dừng", Toast.LENGTH_SHORT).show();
                } else {
                    mediaPlayer.start();
                    Toast.makeText(this, "Âm thanh: Đang phát", Toast.LENGTH_SHORT).show();
                }
                isPlaying = !isPlaying;
            } else {
                Toast.makeText(this, "Chưa có nhạc nền.", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Start
        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
            Toast.makeText(this, "Bắt đầu cuộc đua!", Toast.LENGTH_SHORT).show();
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
        });

        // Nút Exit
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
            Toast.makeText(this, "Thoát game", Toast.LENGTH_SHORT).show();
            finishAffinity();
        });

        // Nút Guide (nếu có)
        Button btnGuide = findViewById(R.id.btnGuide);
        if (btnGuide != null) {
            btnGuide.setOnClickListener(v -> {
                soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
                Toast.makeText(this, "Hướng dẫn đang được cập nhật!", Toast.LENGTH_SHORT).show();
            });
        }



        // Nút xoay màn hình
        Button btnRotate = findViewById(R.id.btnRotate);
        btnRotate.setOnClickListener(v -> {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Toast.makeText(this, "Xoay ngang", Toast.LENGTH_SHORT).show();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Toast.makeText(this, "Xoay dọc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (soundPool != null) {
            soundPool.release();
        }
    }
}
