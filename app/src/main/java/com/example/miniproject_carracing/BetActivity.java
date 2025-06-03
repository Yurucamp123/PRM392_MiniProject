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

public class BetActivity extends AppCompatActivity {

    private BetAdapter adapter;
    private TextView tvBalance;

    // Betting variables
    private int selectedCar = -1;
    private double betAmount = 0;
    private final String[] carNames = {"Xe đua đỏ", "Xe đua đen", "Xe mô tô xanh"};
    private final int[] carNumbers = {1, 2, 3};

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
            Intent intent = new Intent(BetActivity.this, WalletActivity.class);
            startActivityForResult(intent, 100);
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(BetActivity.this, RaceHistoryActivity.class);
            startActivity(intent);
        });

        btnAddBet.setOnClickListener(v -> {
            Log.d("BetActivity", "btnAddBet clicked");
            showSimpleBetDialog();
        });
    }

    private void showSimpleBetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("🏁 Đặt Cược Đua Xe");

        // Tạo view cho dialog
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 30, 50, 30);

        // Hiển thị số dư
        TextView tvCurrentBalance = new TextView(this);
        tvCurrentBalance.setText("Số dư hiện tại: " + formatCurrency(GameSession.balance));
        tvCurrentBalance.setTextSize(16);
        tvCurrentBalance.setPadding(0, 0, 0, 20);
        dialogLayout.addView(tvCurrentBalance);

        // Chọn xe
        TextView tvCarLabel = new TextView(this);
        tvCarLabel.setText("🚗 Chọn xe để đặt cược:");
        tvCarLabel.setTextSize(14);
        tvCarLabel.setPadding(0, 0, 0, 10);
        dialogLayout.addView(tvCarLabel);

        RadioGroup carGroup = new RadioGroup(this);
        carGroup.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < carNames.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(carNames[i] + " (Xe #" + carNumbers[i] + ")");
            radioButton.setId(i);
            radioButton.setPadding(0, 5, 0, 5);
            carGroup.addView(radioButton);
        }
        dialogLayout.addView(carGroup);

        // Nhập số tiền
        TextView tvAmountLabel = new TextView(this);
        tvAmountLabel.setText("💰 Nhập số tiền cược:");
        tvAmountLabel.setTextSize(14);
        tvAmountLabel.setPadding(0, 20, 0, 10);
        dialogLayout.addView(tvAmountLabel);

        final EditText etAmount = new EditText(this);
        etAmount.setHint("Tối thiểu 50.000 VND");
        etAmount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etAmount.setPadding(20, 15, 20, 15);
        dialogLayout.addView(etAmount);

        // Quick buttons
        LinearLayout quickButtonsLayout = new LinearLayout(this);
        quickButtonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        quickButtonsLayout.setPadding(0, 10, 0, 0);

        String[] quickAmounts = {"50K", "100K", "200K", "500K"};
        int[] quickValues = {50000, 100000, 200000, 500000};

        for (int i = 0; i < quickAmounts.length; i++) {
            Button quickBtn = new Button(this);
            quickBtn.setText(quickAmounts[i]);
            quickBtn.setTextSize(12);

            final int value = quickValues[i];
            quickBtn.setOnClickListener(v -> etAmount.setText(String.valueOf(value)));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 0, 5, 0);
            quickBtn.setLayoutParams(params);
            quickButtonsLayout.addView(quickBtn);
        }
        dialogLayout.addView(quickButtonsLayout);

        builder.setView(dialogLayout);

        // Xử lý nút Đặt cược
        builder.setPositiveButton("🎯 Đặt Cược", (dialog, which) -> {
            int checkedId = carGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Vui lòng chọn xe để đặt cược!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedCar = checkedId;
            String amountText = etAmount.getText().toString();

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số tiền cược!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                betAmount = Double.parseDouble(amountText);
                if (validateAndPlaceBet()) {
                    dialog.dismiss();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("❌ Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean validateAndPlaceBet() {
        if (betAmount < 50000) {
            Toast.makeText(this, "Số tiền cược tối thiểu là 50.000 VND!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (betAmount > GameSession.balance) {
            Toast.makeText(this, "Không đủ tiền trong ví!", Toast.LENGTH_SHORT).show();
            return false;
        }

        placeAdvancedBet();
        return true;
    }

    private void placeAdvancedBet() {
        Random rand = new Random();
        int raceId = rand.nextInt(1000) + 1;
        int winningCarIndex = rand.nextInt(3); // 0, 1, or 2
        boolean isWin = (winningCarIndex == selectedCar);

        // Tính toán kết quả
        GameSession.balance -= betAmount;
        double winAmount = 0;
        String result;

        if (isWin) {
            winAmount = betAmount * 2.5; // Tỷ lệ thắng 1:2.5
            GameSession.balance += winAmount;
            result = "THẮNG + " + formatCurrency(winAmount - betAmount);
        } else {
            result = "THUA - " + formatCurrency(betAmount);
        }

        String winningCar = carNames[winningCarIndex];
        String selectedCarName = carNames[selectedCar];
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());

        // Tạo bet với thông tin chi tiết
        Bet newBet = new Bet(raceId, betAmount, result, dateTime, winningCar);
        newBet.setSelectedCar(selectedCarName);

        GameSession.betHistory.add(0, newBet); // Thêm vào đầu list
        adapter.notifyDataSetChanged();
        updateBalanceUI();

        // Hiển thị kết quả chi tiết
        String resultMessage = String.format(
                "%s\nXe bạn chọn: %s\nXe thắng: %s\nSố dư còn lại: %s",
                isWin ? "🎉 CHÚC MỪNG! BẠN ĐÃ THẮNG!" : "😔 RẤT TIẾC! BẠN ĐÃ THUA!",
                selectedCarName,
                winningCar,
                formatCurrency(GameSession.balance)
        );

        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();

        // Reset selection
        selectedCar = -1;
        betAmount = 0;
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