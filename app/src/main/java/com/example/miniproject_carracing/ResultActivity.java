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
    Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvWinner = (TextView) findViewById(R.id.tvWinner);
        tvBetResult = (TextView) findViewById(R.id.tvBetResult);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain1);
        Intent intent = getIntent();
        String winner = intent.getStringExtra("WINNER");
        boolean isBetCorrect = intent.getBooleanExtra("BET_RESULT", false);
        int rewardAmount = intent.getIntExtra("REWARD_AMOUNT", 0);

        tvWinner.setText("Winner: " + winner);
        String formattedAmount = NumberFormat.getInstance(new Locale("vi", "VN")).format(rewardAmount);

        if (isBetCorrect) {
            tvBetResult.setText("Bạn đã thắng: " + formattedAmount + " VNĐ");
        }

        btnPlayAgain.setOnClickListener(v -> {
            Intent i = new Intent(ResultActivity.this, RacingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}

