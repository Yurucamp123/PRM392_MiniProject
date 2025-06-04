package com.example.miniproject_carracing;

import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.GameSession;
import model.Transaction;

public class WalletActivity extends AppCompatActivity {

    private TextView tvBalance;
    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private Button btnDeposit, btnWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        tvBalance = findViewById(R.id.tvWalletBalance);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        rvTransactions = findViewById(R.id.rvTransactions);

        adapter = new TransactionAdapter(GameSession.transactionHistory);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        rvTransactions.setAdapter(adapter);

        updateBalanceText();

        btnDeposit.setOnClickListener(v -> showMoneyDialog(true));
        btnWithdraw.setOnClickListener(v -> showMoneyDialog(false));
    }

    private void showMoneyDialog(boolean isDeposit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isDeposit ? "Nạp tiền" : "Rút tiền");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Nhập số tiền");
        builder.setView(input);

        builder.setPositiveButton(isDeposit ? "Nạp" : "Rút", (dialog, which) -> {
            try {
                double amount = Double.parseDouble(input.getText().toString());
                if (amount <= 0) {
                    Toast.makeText(this, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                String now = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                if (isDeposit) {
                    GameSession.balance += amount;
                    GameSession.transactionHistory.add(new Transaction("NẠP TIỀN", amount, now));
                } else {
                    if (amount > GameSession.balance) {
                        Toast.makeText(this, "Không đủ tiền để rút", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GameSession.balance -= amount;
                    GameSession.transactionHistory.add(new Transaction("RÚT TIỀN", amount, now));
                }

                adapter.notifyDataSetChanged();
                updateBalanceText();
                setResult(RESULT_OK);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Sai định dạng", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateBalanceText() {
        tvBalance.setText("Số dư: " + formatCurrency(GameSession.balance));
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }
}
