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


    private final String[] carNames = {"Xe đua đỏ", "Xe đua đen", "Xe mô tô xanh"};
    private final int[] carNumbers = {1, 2, 3};


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

        initViews();
        setupAdapter();
        setupClickListeners();
        updateBalanceUI();
    }

    private void initViews() {
        tvBalance = findViewById(R.id.tvBalance);
    }

    private void setupAdapter() {
        RecyclerView rv = findViewById(R.id.recyclerView);
        adapter = new BetAdapter(GameSession.betHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void setupClickListeners() {
        Button btnWallet = findViewById(R.id.btnWallet);
        btnWallet.setOnClickListener(v -> {
            Intent intent = new Intent(BetActivity.this, WalletActivity.class);
            startActivityForResult(intent, 100);
        });

        tvBalance.setOnClickListener(v -> showMultipleBetDialog());

    }

    private void showMultipleBetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("🏁 Đặt Cược Đua Xe (Tối đa 2 xe)");


        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 30, 50, 30);


        TextView tvCurrentBalance = new TextView(this);
        tvCurrentBalance.setText("Số dư hiện tại: " + formatCurrency(GameSession.balance));
        tvCurrentBalance.setTextSize(16);
        tvCurrentBalance.setPadding(0, 0, 0, 20);
        dialogLayout.addView(tvCurrentBalance);


        LinearLayout betsContainer = new LinearLayout(this);
        betsContainer.setOrientation(LinearLayout.VERTICAL);


        LinearLayout bet1Layout = createBetLayout("🚗 Cược 1:", 1);
        betsContainer.addView(bet1Layout);


        View divider = new View(this);
        divider.setBackgroundColor(0xFFCCCCCC);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        dividerParams.setMargins(0, 20, 0, 20);
        divider.setLayoutParams(dividerParams);
        betsContainer.addView(divider);


        LinearLayout bet2Layout = createBetLayout("🏎️ Cược 2 (Tùy chọn):", 2);
        betsContainer.addView(bet2Layout);

        dialogLayout.addView(betsContainer);

        builder.setView(dialogLayout);


        builder.setPositiveButton("🎯 Đặt Cược", (dialog, which) -> {
            if (processMultipleBets(bet1Layout, bet2Layout)) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("❌ Hủy", (dialog, which) -> dialog.cancel());


        builder.setNeutralButton("🏁 Về Đua Xe", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(BetActivity.this, RacingActivity.class);
            startActivity(intent);
            finish();
        });

        builder.show();
    }

    private LinearLayout createBetLayout(String title, int betNumber) {
        LinearLayout betLayout = new LinearLayout(this);
        betLayout.setOrientation(LinearLayout.VERTICAL);
        betLayout.setPadding(10, 10, 10, 10);


        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(16);
        tvTitle.setPadding(0, 0, 0, 10);
        betLayout.addView(tvTitle);


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


        BetInfo bet1 = getBetInfoFromLayout(bet1Layout, 1);
        if (bet1 != null) {
            bets.add(bet1);
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin cho cược 1!", Toast.LENGTH_SHORT).show();
            return false;
        }

        BetInfo bet2 = getBetInfoFromLayout(bet2Layout, 2);
        if (bet2 != null) {
            bets.add(bet2);
        }


        if (bets.size() == 2 && bets.get(0).carIndex == bets.get(1).carIndex) {
            Toast.makeText(this, "Không thể đặt cược 2 xe giống nhau!", Toast.LENGTH_SHORT).show();
            return false;
        }


        double totalAmount = 0;
        for (BetInfo bet : bets) {
            totalAmount += bet.amount;
        }

        if (totalAmount > GameSession.balance) {
            Toast.makeText(this, "Không đủ tiền trong ví! Tổng cược: " + formatCurrency(totalAmount), Toast.LENGTH_SHORT).show();
            return false;
        }


        placeMultipleBets(bets);
        return true;
    }

    private BetInfo getBetInfoFromLayout(LinearLayout betLayout, int betNumber) {

        RadioGroup carGroup = betLayout.findViewWithTag("carGroup" + betNumber);
        int checkedId = carGroup.getCheckedRadioButtonId();

        if (checkedId == -1) {
            return null;
        }


        EditText etAmount = betLayout.findViewWithTag("amount" + betNumber);
        String amountText = etAmount.getText().toString();

        if (amountText.isEmpty()) {
            return null;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 50000) {
                Toast.makeText(this, "Số tiền cược tối thiểu là 50.000 VND!", Toast.LENGTH_SHORT).show();
                return null;
            }

            int carIndex = checkedId % 10;
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
        int winningCarIndex = rand.nextInt(3);
        String winningCarName = carNames[winningCarIndex];
        double odds = 2.5;


        double totalBetAmount = 0;
        for (BetInfo bet : bets) {
            totalBetAmount += bet.amount;
        }


        GameSession.balance -= totalBetAmount;

        double totalWinAmount = 0;
        StringBuilder resultDetails = new StringBuilder();
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());


        for (int i = 0; i < bets.size(); i++) {
            BetInfo bet = bets.get(i);
            boolean isWin = (bet.carIndex == winningCarIndex);

            String betResult;
            String result;
            if (isWin) {
                double winAmount = bet.amount;
                GameSession.balance += winAmount;
                totalWinAmount += winAmount;
                betResult = "THẮNG (Lấy lại: " + formatCurrency(winAmount) + ")";
                result = "THẮNG +" + formatCurrency(winAmount);
            } else {
                betResult = "THUA (Mất: " + formatCurrency(bet.amount) + ")";
                result = "THUA -" + formatCurrency(bet.amount);
            }

            Bet newBet = new Bet(raceId, bet.amount, result, dateTime, winningCarName, bet.carName, odds);
            GameSession.betHistory.add(0, newBet);
            resultDetails.append(String.format("Cược %d: %s - %s\n", i + 1, bet.carName, betResult));
        }

        adapter.notifyDataSetChanged();
        updateBalanceUI();


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
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalanceUI();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BetActivity.this, RacingActivity.class);
        startActivity(intent);
        finish();
    }
}