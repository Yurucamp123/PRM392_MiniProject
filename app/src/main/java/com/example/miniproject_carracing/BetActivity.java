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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Bet;
import model.GameSession;

public class BetActivity extends AppCompatActivity {

    private BetAdapter adapter;
    private TextView tvBalance;

    // Betting variables
    private final String[] carNames = {"Xe đua đỏ", "Xe đua đen", "Xe mô tô xanh"};
    private final int[] carNumbers = {1, 2, 3};

    // Class để lưu thông tin cược
    private static class BetInfo {
        int carIndex;
        double amount;
        String carName;

        BetInfo(int carIndex, double amount, String carName) {
            this.carIndex = carIndex;
            this.amount = amount;
            this.carName = carName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        RecyclerView rv = findViewById(R.id.recyclerView);
//        Button btnAddBet = findViewById(R.id.btnAddBet);
        Button btnWallet = findViewById(R.id.btnWallet);

        tvBalance = findViewById(R.id.tvBalance);

        adapter = new BetAdapter(GameSession.betHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        updateBalanceUI();

        btnWallet.setOnClickListener(v -> {
            Intent intent = new Intent(BetActivity.this, WalletActivity.class);
            startActivityForResult(intent, 100);
        });

//        btnAddBet.setOnClickListener(v -> {
//            Log.d("BetActivity", "btnAddBet clicked");
//            showMultipleBetDialog();
//        });
    }

    private void showMultipleBetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("🏁 Đặt Cược Đua Xe (Tối đa 2 xe)");

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

        // Container cho các cược
        LinearLayout betsContainer = new LinearLayout(this);
        betsContainer.setOrientation(LinearLayout.VERTICAL);

        // Cược 1
        LinearLayout bet1Layout = createBetLayout("🚗 Cược 1:", 1);
        betsContainer.addView(bet1Layout);

        // Divider
        View divider = new View(this);
        divider.setBackgroundColor(0xFFCCCCCC);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        dividerParams.setMargins(0, 20, 0, 20);
        divider.setLayoutParams(dividerParams);
        betsContainer.addView(divider);

        // Cược 2
        LinearLayout bet2Layout = createBetLayout("🏎️ Cược 2 (Tùy chọn):", 2);
        betsContainer.addView(bet2Layout);

        dialogLayout.addView(betsContainer);

        builder.setView(dialogLayout);

        // Xử lý nút Đặt cược
        builder.setPositiveButton("🎯 Đặt Cược", (dialog, which) -> {
            if (processMultipleBets(bet1Layout, bet2Layout)) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("❌ Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private LinearLayout createBetLayout(String title, int betNumber) {
        LinearLayout betLayout = new LinearLayout(this);
        betLayout.setOrientation(LinearLayout.VERTICAL);
        betLayout.setPadding(10, 10, 10, 10);

        // Title
        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(16);
        tvTitle.setPadding(0, 0, 0, 10);
        betLayout.addView(tvTitle);

        // Chọn xe
        TextView tvCarLabel = new TextView(this);
        tvCarLabel.setText("Chọn xe:");
        tvCarLabel.setTextSize(14);
        tvCarLabel.setPadding(0, 0, 0, 5);
        betLayout.addView(tvCarLabel);

        RadioGroup carGroup = new RadioGroup(this);
        carGroup.setOrientation(LinearLayout.HORIZONTAL);
        carGroup.setTag("carGroup" + betNumber);

        for (int i = 0; i < carNames.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(carNames[i]);
            radioButton.setId(betNumber * 10 + i); // Unique ID
            radioButton.setPadding(0, 5, 0, 5);
            carGroup.addView(radioButton);
        }
        betLayout.addView(carGroup);

        // Nhập số tiền
        TextView tvAmountLabel = new TextView(this);
        tvAmountLabel.setText("💰 Số tiền cược:");
        tvAmountLabel.setTextSize(14);
        tvAmountLabel.setPadding(0, 10, 0, 5);
        betLayout.addView(tvAmountLabel);

        final EditText etAmount = new EditText(this);
        etAmount.setHint("Tối thiểu 50.000 VND");
        etAmount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etAmount.setPadding(20, 15, 20, 15);
        etAmount.setTag("amount" + betNumber);
        betLayout.addView(etAmount);

        // Quick buttons
        LinearLayout quickButtonsLayout = new LinearLayout(this);
        quickButtonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        quickButtonsLayout.setPadding(0, 5, 0, 0);

        String[] quickAmounts = {"50K", "100K", "200K", "500K"};
        int[] quickValues = {50000, 100000, 200000, 500000};

        for (int i = 0; i < quickAmounts.length; i++) {
            Button quickBtn = new Button(this);
            quickBtn.setText(quickAmounts[i]);
            quickBtn.setTextSize(10);

            final int value = quickValues[i];
            quickBtn.setOnClickListener(v -> etAmount.setText(String.valueOf(value)));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(2, 0, 2, 0);
            quickBtn.setLayoutParams(params);
            quickButtonsLayout.addView(quickBtn);
        }
        betLayout.addView(quickButtonsLayout);

        return betLayout;
    }

    private boolean processMultipleBets(LinearLayout bet1Layout, LinearLayout bet2Layout) {
        List<BetInfo> bets = new ArrayList<>();

        // Xử lý cược 1
        BetInfo bet1 = getBetInfoFromLayout(bet1Layout, 1);
        if (bet1 != null) {
            bets.add(bet1);
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin cho cược 1!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Xử lý cược 2 (tùy chọn)
        BetInfo bet2 = getBetInfoFromLayout(bet2Layout, 2);
        if (bet2 != null) {
            bets.add(bet2);
        }

        // Kiểm tra trùng xe
        if (bets.size() == 2 && bets.get(0).carIndex == bets.get(1).carIndex) {
            Toast.makeText(this, "Không thể đặt cược 2 xe giống nhau!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra tổng số tiền
        double totalAmount = 0;
        for (BetInfo bet : bets) {
            totalAmount += bet.amount;
        }

        if (totalAmount > GameSession.balance) {
            Toast.makeText(this, "Không đủ tiền trong ví! Tổng cược: " + formatCurrency(totalAmount), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Thực hiện đặt cược
        placeMultipleBets(bets);
        return true;
    }

    private BetInfo getBetInfoFromLayout(LinearLayout betLayout, int betNumber) {
        // Lấy RadioGroup
        RadioGroup carGroup = betLayout.findViewWithTag("carGroup" + betNumber);
        int checkedId = carGroup.getCheckedRadioButtonId();

        if (checkedId == -1) {
            return null; // Không chọn xe
        }

        // Lấy EditText
        EditText etAmount = betLayout.findViewWithTag("amount" + betNumber);
        String amountText = etAmount.getText().toString();

        if (amountText.isEmpty()) {
            return null; // Không nhập số tiền
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 50000) {
                Toast.makeText(this, "Số tiền cược tối thiểu là 50.000 VND!", Toast.LENGTH_SHORT).show();
                return null;
            }

            int carIndex = checkedId % 10; // Lấy chỉ số xe từ ID
            String carName = carNames[carIndex];

            return new BetInfo(carIndex, amount, carName);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void placeMultipleBets(List<BetInfo> bets) {
        Random rand = new Random();
        int raceId = rand.nextInt(1000) + 1;
        int winningCarIndex = rand.nextInt(3); // 0, 1, or 2
        String winningCarName = carNames[winningCarIndex];

        // Tính toán tổng số tiền cược
        double totalBetAmount = 0;
        for (BetInfo bet : bets) {
            totalBetAmount += bet.amount;
        }

        // Trừ tổng số tiền cược từ balance
        GameSession.balance -= totalBetAmount;

        double totalWinAmount = 0;
        StringBuilder resultDetails = new StringBuilder();
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());

        // Xử lý từng cược
        for (int i = 0; i < bets.size(); i++) {
            BetInfo bet = bets.get(i);
            boolean isWin = (bet.carIndex == winningCarIndex);

            String betResult;
            if (isWin) {
                // Xe thắng: được cộng lại số tiền đã đặt (lấy lại tiền gốc)
                double winAmount = bet.amount;
                GameSession.balance += winAmount;
                totalWinAmount += winAmount;
                betResult = "THẮNG (Lấy lại: " + formatCurrency(winAmount) + ")";
            } else {
                // Xe thua: mất tiền cược (đã trừ ở trên)
                betResult = "THUA (Mất: " + formatCurrency(bet.amount) + ")";
            }

            // Thêm vào lịch sử
            Bet newBet = new Bet(raceId, bet.amount, isWin ? "WIN" : "LOSE", dateTime, winningCarName);
            newBet.setSelectedCar(bet.carName);
            GameSession.betHistory.add(0, newBet);

            // Thêm vào chi tiết kết quả
            resultDetails.append(String.format("Cược %d: %s - %s\n", i + 1, bet.carName, betResult));
        }

        adapter.notifyDataSetChanged();
        updateBalanceUI();

        // Hiển thị kết quả tổng hợp
        double netResult = totalWinAmount - totalBetAmount;
        String summaryMessage = String.format(
                "%s\n\n%s\nXe thắng: %s\n\nKết quả: %s\nSố dư còn lại: %s",
                netResult >= 0 ? "🎉 CHÚC MỪNG!" : "😔 RẤT TIẾC!",
                resultDetails.toString().trim(),
                winningCarName,
                netResult >= 0 ? "+" + formatCurrency(Math.abs(netResult)) : "-" + formatCurrency(Math.abs(netResult)),
                formatCurrency(GameSession.balance)
        );

        Toast.makeText(this, summaryMessage, Toast.LENGTH_LONG).show();
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