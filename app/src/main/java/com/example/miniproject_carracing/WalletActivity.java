package com.example.miniproject_carracing;

import android.os.Bundle;
import android.text.InputType;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import model.GameSession;
import model.Transaction;

public class WalletActivity extends AppCompatActivity {

    private TextView tvBalance;
    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private Button btnDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        tvBalance = findViewById(R.id.tvBalance);
        btnDeposit = findViewById(R.id.btnDeposit);
        rvTransactions = findViewById(R.id.rvTransactions);

        adapter = new TransactionAdapter(GameSession.transactionHistory);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        rvTransactions.setAdapter(adapter);

        updateBalanceText();

        btnDeposit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nạp tiền");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Nhập số tiền cần nạp");
            builder.setView(input);

            builder.setPositiveButton("Nạp", (dialog, which) -> {
                try {
                    double amount = Double.parseDouble(input.getText().toString());
                    if (amount <= 0) {
                        Toast.makeText(this, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GameSession.balance += amount;
                    GameSession.transactionHistory.add(new Transaction("NẠP TIỀN", amount));
                    adapter.notifyDataSetChanged();
                    updateBalanceText();
                    setResult(RESULT_OK); // Báo MainActivity cập nhật
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Sai định dạng", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void updateBalanceText() {
        tvBalance.setText("Số dư: " + formatCurrency(GameSession.balance));
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }
}
