package com.example.miniproject_carracing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    TextView tvWinner, tvBetResult;
    Button btnPlayAgain, btnBackToStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Ánh xạ view
        tvWinner = findViewById(R.id.tvWinner);
        tvBetResult = findViewById(R.id.tvBetResult);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnBackToStart = findViewById(R.id.btnBackToStart); // 🟢 THÊM ánh xạ nút exit

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String winner = intent.getStringExtra("WINNER");
        boolean isBetCorrect = intent.getBooleanExtra("BET_RESULT", false);
        int rewardAmount = intent.getIntExtra("REWARD_AMOUNT", 0); // 🟢 nhận số tiền thắng

        tvWinner.setText("Winner: " + winner);

        if (isBetCorrect) {
            String formattedAmount = NumberFormat.getInstance(new Locale("vi", "VN")).format(rewardAmount);
            tvBetResult.setText("Bạn đã thắng: " + formattedAmount + " VNĐ");
        } else {
            tvBetResult.setText("Bạn đã thua cược!");
        }

        // Nút chơi lại
        btnPlayAgain.setOnClickListener(v -> {
            Intent i = new Intent(ResultActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });

        // Nút quay lại màn hình bắt đầu
        btnBackToStart.setOnClickListener(v -> {
            Intent i = new Intent(ResultActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}
