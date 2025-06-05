package com.example.miniproject_carracing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_REGISTER = 1001;

    ImageButton btnVolume;
    Button btnStart;
    boolean isPlaying = true;
    private boolean isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        } else {
            setContentView(R.layout.activity_start);
        }

        MusicManager.getInstance().playMusic(this, R.raw.intro);
        Toast.makeText(this, "🔊 Âm thanh: Đang phát", Toast.LENGTH_SHORT).show();

        btnVolume = findViewById(R.id.btnVolume);
        btnVolume.setOnClickListener(v -> {
            MusicManager manager = MusicManager.getInstance();
            if (manager.isPlaying()) {
                manager.pause();
                Toast.makeText(this, "🔇 Âm thanh đã tắt", Toast.LENGTH_SHORT).show();
            } else {
                manager.resume();
                Toast.makeText(this, "🔊 Âm thanh đã bật", Toast.LENGTH_SHORT).show();
            }
        });

        btnStart = findViewById(R.id.btnStart);
        updateStartButtonState();
        btnStart.setOnClickListener(v -> {
            MusicManager.getInstance().stop();

            startActivity(new Intent(this, RacingActivity.class));
        });

        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            Toast.makeText(this, "Thoát game", Toast.LENGTH_SHORT).show();
            MusicManager.getInstance().stop();  // Dừng nhạc nếu thoát hẳn
            finishAffinity();
        });

        Button btnGuide = findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(v -> {
            startActivity(new Intent(this, GuideActivity.class));
        });

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

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER);
        });

        if (savedInstanceState != null) {
            isRegistered = savedInstanceState.getBoolean("isRegistered", false);
            updateStartButtonState();
        }
    }

    private void updateStartButtonState() {
        if (btnStart != null) {
            btnStart.setEnabled(isRegistered);
            btnStart.setAlpha(isRegistered ? 1.0f : 0.5f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            isRegistered = true;
            updateStartButtonState();
            Toast.makeText(this, "✅ Đăng ký thành công! Bạn có thể bắt đầu chơi.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isRegistered", isRegistered);
    }
}
