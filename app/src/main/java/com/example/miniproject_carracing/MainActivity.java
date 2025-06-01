package com.example.miniproject_carracing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import model.Bet;
import model.GameSession;

public class MainActivity extends AppCompatActivity {

    private BetAdapter adapter;
    private TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.recyclerView);
        Button btnAddBet = findViewById(R.id.btnAddBet);
        Button btnWallet = findViewById(R.id.btnWallet);
        Button btnViewHistory = findViewById(R.id.btnViewHistory);

        tvBalance = findViewById(R.id.tvBalance);

        adapter = new BetAdapter(GameSession.betHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        updateBalanceUI();

        btnWallet.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WalletActivity.class);
            startActivityForResult(intent, 100); // Mở WalletActivity và đợi kết quả
        });
        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RaceHistoryActivity.class);
            startActivity(intent);
        });


        btnAddBet.setOnClickListener(v -> {
            Log.d("MainActivity", "btnAddBet clicked");
            showBetDialog();
        });

    }

    private void showBetDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Đặt cược");

        final EditText input = new EditText(this);
        input.setHint("Nhập số tiền cược:");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String inputText = input.getText().toString();
            if (!inputText.isEmpty()) {
                try {
                    double amount = Double.parseDouble(inputText);
                    addUserBet(amount);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Số không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addUserBet(double amount) {
        if (amount < 50000) {
            Toast.makeText(this, "Số tiền cược tối thiểu là 50.000 VND!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > GameSession.balance) {
            Toast.makeText(this, "Không đủ tiền trong ví!", Toast.LENGTH_SHORT).show();
            return;
        }

        Random rand = new Random();
        int raceId = rand.nextInt(100) + 1;
        boolean isWin = rand.nextBoolean();
        String result = isWin ? "THẮNG + " + formatCurrency(amount) : "THUA - " + formatCurrency(amount);
        String winningCar = "Xe số " + (rand.nextInt(10) + 1); // giả sử có 10 xe
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());

        GameSession.balance -= amount;
        if (isWin) {
            GameSession.balance += amount * 2;
        }

        GameSession.betHistory.add(new Bet(raceId, amount, result, dateTime, winningCar));
        adapter.notifyDataSetChanged();
        updateBalanceUI();

        Toast.makeText(this, "Còn lại: " + formatCurrency(GameSession.balance), Toast.LENGTH_SHORT).show();
    }



    private void updateBalanceUI() {
        tvBalance.setText("Số dư: " + formatCurrency(GameSession.balance));
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            updateBalanceUI();
        }
    }
}
