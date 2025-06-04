package com.example.miniproject_carracing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    TextView tvWinner, tvBetResult;
    Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvWinner = findViewById(R.id.tvWinner);
        tvBetResult = findViewById(R.id.tvBetResult);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String winner = intent.getStringExtra("WINNER");
        boolean isBetCorrect = intent.getBooleanExtra("BET_RESULT", false);

        tvWinner.setText("Winner: " + winner);
        tvBetResult.setText(isBetCorrect ? "Bạn đã thắng cược!" : "Bạn đã thua cược!");

        btnPlayAgain.setOnClickListener(v -> {
            Intent i = new Intent(ResultActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}

