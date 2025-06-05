package com.example.miniproject_carracing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultLoseActivity extends AppCompatActivity {
    TextView tvLoser, tvBetResult;
    Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_lose);

        tvLoser = findViewById(R.id.tvLoser);
        tvBetResult = findViewById(R.id.tvBetResult);
        btnPlayAgain = findViewById(R.id.btnPlayAgain2);

        Intent intent = getIntent();
        boolean isBetCorrect = intent.getBooleanExtra("BET_RESULT", false);
        int rewardAmount = intent.getIntExtra("LOST_AMOUNT", 0);
        String formattedAmount = NumberFormat.getInstance(new Locale("vi", "VN")).format(rewardAmount);
        tvLoser.setText("Thua cuộc!");
        if (!isBetCorrect) {
            tvBetResult.setText("Bạn đã thua: " + formattedAmount + " VNĐ");
        }
        btnPlayAgain.setOnClickListener(v -> {
            Intent i = new Intent(ResultLoseActivity.this, RacingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}


