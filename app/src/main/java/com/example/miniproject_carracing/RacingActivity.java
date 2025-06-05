package com.example.miniproject_carracing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

import model.Bet;
import model.GameSession;

public class RacingActivity extends AppCompatActivity {

    private SeekBar mSeekBar1, mSeekBar2, mSeekBar3;
    private CheckBox mCheckBox1, mCheckBox2, mCheckBox3;
    private EditText mEtBetAmount1, mEtBetAmount2, mEtBetAmount3;
    private TextView mTvCountDown, mTvResult, mTvPlayerMoney, mTvTotalBet;
    private ImageView mResultImage;
    private Dialog mDialog;
    private Button mButtonPlayAgain, mButtonExit;
    private CountDownTimer mCountDownTimer2;
    private Handler mHandler;
    private Runnable mRunnable;
    private int mCountDownNumber;
    private int mTotalBet = 0;
    private boolean isBetConfirmed = false;
    private MediaPlayer mpButton, mpRacingEffect, mpCount, mpError, mpWin, mpLose, mpMusic;
    private final String[] carNames = {"Xe đua đỏ", "Xe đua đen", "Xe mô tô xanh"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing);
        initViews();
        initMediaPlayers();
        setupCheckboxListeners();
        setupButtonListeners();
        updateMoneyUI();
    }
    private void initViews() {
        mCheckBox1 = findViewById(R.id.checkbox_1);
        mCheckBox2 = findViewById(R.id.checkbox_2);
        mCheckBox3 = findViewById(R.id.checkbox_3);

        mEtBetAmount1 = findViewById(R.id.et_bet_amount_1);
        mEtBetAmount2 = findViewById(R.id.et_bet_amount_2);
        mEtBetAmount3 = findViewById(R.id.et_bet_amount_3);

        mSeekBar1 = findViewById(R.id.seekBar1);
        mSeekBar2 = findViewById(R.id.seekBar2);
        mSeekBar3 = findViewById(R.id.seekBar3);

        mTvCountDown = findViewById(R.id.countDownNumber);
        mTvPlayerMoney = findViewById(R.id.tv_player_money);
        mTvTotalBet = findViewById(R.id.tv_total_bet);

        mHandler = new Handler();
    }
    private void initMediaPlayers() {
        mpButton = MediaPlayer.create(this, R.raw.game_button);
        mpRacingEffect = MediaPlayer.create(this, R.raw.game_car_racing_effect);
        mpCount = MediaPlayer.create(this, R.raw.game_count);
        mpError = MediaPlayer.create(this, R.raw.game_error);
        mpWin = MediaPlayer.create(this, R.raw.game_win);
        mpLose = MediaPlayer.create(this, R.raw.game_lose);
        mpMusic = MediaPlayer.create(this, R.raw.game_music);
        mpMusic.setLooping(true);
        mpMusic.start();
    }
    private void setupCheckboxListeners() {
        CompoundButton.OnCheckedChangeListener checkLimitListener = (buttonView, isChecked) -> {
            int countChecked = 0;
            if (mCheckBox1.isChecked()) countChecked++;
            if (mCheckBox2.isChecked()) countChecked++;
            if (mCheckBox3.isChecked()) countChecked++;
            if (countChecked > 2) {
                buttonView.setChecked(false);
                Toast.makeText(this, "Chỉ được cược tối đa 2 xe", Toast.LENGTH_SHORT).show();
                playSound(mpError);
            }
        };
        mCheckBox1.setOnCheckedChangeListener(checkLimitListener);
        mCheckBox2.setOnCheckedChangeListener(checkLimitListener);
        mCheckBox3.setOnCheckedChangeListener(checkLimitListener);
    }
    private void setupButtonListeners() {
        Button btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            playSound(mpButton);
            finish();
        });

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            playSound(mpButton);
            if (!isBetConfirmed) {
                playSound(mpError);
                Toast.makeText(this, "Vui lòng xác nhận đặt cược trước khi bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }
            startPlaying();
        });

        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> {
            playSound(mpButton);
            resetGame();
        });

        Button btnBet = findViewById(R.id.btn_bet);
        btnBet.setOnClickListener(v -> {
            playSound(mpButton);
            showConfirmBetDialog();
        });

        Button btnWallet = findViewById(R.id.btn_wallet);
        btnWallet.setOnClickListener(v -> {
            playSound(mpButton);
            Intent intent = new Intent(RacingActivity.this, BetActivity.class);
            startActivity(intent);
        });
    }
    private void showConfirmBetDialog() {
        int bet1 = parseBetAmount(mEtBetAmount1.getText().toString());
        int bet2 = parseBetAmount(mEtBetAmount2.getText().toString());
        int bet3 = parseBetAmount(mEtBetAmount3.getText().toString());

        if (!mCheckBox1.isChecked() && !mCheckBox2.isChecked() && !mCheckBox3.isChecked()) {
            playSound(mpError);
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 xe để đặt cược", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((bet1 > 0 && !mCheckBox1.isChecked()) || (bet2 > 0 && !mCheckBox2.isChecked()) || (bet3 > 0 && !mCheckBox3.isChecked())) {
            playSound(mpError);
            Toast.makeText(this, "Xe không được chọn không được đặt cược", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalBetTemp = bet1 + bet2 + bet3;
        if (totalBetTemp == 0) {
            playSound(mpError);
            Toast.makeText(this, "Vui lòng nhập số tiền cược", Toast.LENGTH_SHORT).show();
            return;
        }

        if(totalBetTemp < 10000){
            playSound(mpError);
            Toast.makeText(this,"Đặt cược tối thiểu 10.000 VND", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bet1 % 1000 != 0 || bet2 % 1000 != 0 || bet3 % 1000 != 0) {
            playSound(mpError);
            Toast.makeText(this, "Tiền cược phải là bội số của 1.000 VND", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBetTemp > GameSession.balance) {
            playSound(mpError);
            Toast.makeText(this, "Số tiền cược vượt quá số tiền hiện có", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đặt cược")
                .setMessage("Bạn có muốn đặt cược tổng cộng " + formatCurrency(totalBetTemp) + " không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    mTotalBet = totalBetTemp;
                    isBetConfirmed = true;
                    Toast.makeText(RacingActivity.this, "Đặt cược thành công", Toast.LENGTH_SHORT).show();
                    setControlsEnabled(false);
                    updateMoneyUI();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void startPlaying() {
        mSeekBar1.setProgress(0);
        mSeekBar2.setProgress(0);
        mSeekBar3.setProgress(0);

        mCountDownNumber = 4;
        mTvCountDown.setVisibility(View.VISIBLE);
        playSound(mpCount);

        mCountDownTimer2 = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDownNumber--;
                mTvCountDown.setText("" + mCountDownNumber);
            }

            @Override
            public void onFinish() {
                mTvCountDown.setVisibility(View.INVISIBLE);
                playGame();
            }
        };
        mCountDownTimer2.start();
    }
    private void playGame() {
        playSound(mpRacingEffect);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeekBar1.setProgress(mSeekBar1.getProgress() + getRandomNumber(3));
                mSeekBar2.setProgress(mSeekBar2.getProgress() + getRandomNumber(3));
                mSeekBar3.setProgress(mSeekBar3.getProgress() + getRandomNumber(3));

                boolean finished = mSeekBar1.getProgress() >= 100 ||
                        mSeekBar2.getProgress() >= 100 ||
                        mSeekBar3.getProgress() >= 100;

                if (finished) {
                    mHandler.removeCallbacks(this);
                    setControlsEnabled(true);

                    if (mpRacingEffect != null && mpRacingEffect.isPlaying()) {
                        mpRacingEffect.stop();
                        try {
                            mpRacingEffect.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    int winner = 0;
                    int maxProgress = Math.max(mSeekBar1.getProgress(),
                            Math.max(mSeekBar2.getProgress(), mSeekBar3.getProgress()));

                    if (mSeekBar1.getProgress() == maxProgress) winner = 1;
                    else if (mSeekBar2.getProgress() == maxProgress) winner = 2;
                    else if (mSeekBar3.getProgress() == maxProgress) winner = 3;
                    double oldBalance = GameSession.balance;
                    double totalBetAmount = mTotalBet;
                    double totalWinAmount = 0;
                    double totalLostAmount = 0;
                    if (winner == 1 && mCheckBox1.isChecked()) {
                        totalWinAmount += parseBetAmount(mEtBetAmount1.getText().toString());
                    } else if (mCheckBox1.isChecked()) {
                        totalLostAmount += parseBetAmount(mEtBetAmount1.getText().toString());
                    }

                    if (winner == 2 && mCheckBox2.isChecked()) {
                        totalWinAmount += parseBetAmount(mEtBetAmount2.getText().toString());
                    } else if (mCheckBox2.isChecked()) {
                        totalLostAmount += parseBetAmount(mEtBetAmount2.getText().toString());
                    }

                    if (winner == 3 && mCheckBox3.isChecked()) {
                        totalWinAmount += parseBetAmount(mEtBetAmount3.getText().toString());
                    } else if (mCheckBox3.isChecked()) {
                        totalLostAmount += parseBetAmount(mEtBetAmount3.getText().toString());
                    }
                    boolean isWin = totalWinAmount >= totalLostAmount;
                    if (isWin) {
                        playSound(mpWin);
                        Intent intent = new Intent(RacingActivity.this, ResultActivity.class);
                        intent.putExtra("WINNER", carNames[winner - 1]);
                        intent.putExtra("BET_RESULT", true); // Win
                        intent.putExtra("REWARD_AMOUNT", (int) (totalWinAmount - totalLostAmount));
                        saveRaceHistory(winner, totalBetAmount, (totalWinAmount - totalLostAmount));
                        GameSession.balance += (totalWinAmount - totalLostAmount);
                        startActivity(intent);
                    } else {
                        playSound(mpLose);
                        Intent intent = new Intent(RacingActivity.this, ResultLoseActivity.class);
                        intent.putExtra("BET_RESULT", false); // Lose
                        intent.putExtra("LOST_AMOUNT", (int) (totalLostAmount - totalWinAmount));
                        saveRaceHistory(winner, totalBetAmount, totalLostAmount - totalWinAmount);
                        GameSession.balance -= (totalLostAmount - totalWinAmount);
                        startActivity(intent);
                    }
                    updateMoneyUI();
                    isBetConfirmed = false;
                    mTotalBet = 0;
                } else {
                    mHandler.postDelayed(this, 100);
                }
            }
        };

        mHandler.post(mRunnable);
    }
    private void saveRaceHistory(int winner, double totalBetAmount, double totalWinAmount) {
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
        Random rand = new Random();
        int raceId = rand.nextInt(1000) + 1;
        String winningCarName = carNames[winner - 1];
        double odds = 2.0;

        if (mCheckBox1.isChecked()) {
            double betAmount = parseBetAmount(mEtBetAmount1.getText().toString());
            boolean isWin = (winner == 1);
            String result = isWin ? "THẮNG +" + formatCurrency(betAmount) : "THUA -" + formatCurrency(betAmount);
            Bet bet = new Bet(raceId, betAmount, result, dateTime, winningCarName, carNames[0], odds);
            GameSession.betHistory.add(0, bet);
        }

        if (mCheckBox2.isChecked()) {
            double betAmount = parseBetAmount(mEtBetAmount2.getText().toString());
            boolean isWin = (winner == 2);
            String result = isWin ? "THẮNG +" + formatCurrency(betAmount) : "THUA -" + formatCurrency(betAmount);
            Bet bet = new Bet(raceId, betAmount, result, dateTime, winningCarName, carNames[1], odds);
            GameSession.betHistory.add(0, bet);
        }

        if (mCheckBox3.isChecked()) {
            double betAmount = parseBetAmount(mEtBetAmount3.getText().toString());
            boolean isWin = (winner == 3);
            String result = isWin ? "THẮNG +" + formatCurrency(betAmount) : "THUA -" + formatCurrency(betAmount);
            Bet bet = new Bet(raceId, betAmount, result, dateTime, winningCarName, carNames[2], odds);
            GameSession.betHistory.add(0, bet);
        }
    }
    private void playSound(MediaPlayer mp) {
        if (mp != null) {
            mp.seekTo(0);
            mp.start();
        }
    }
    private int parseBetAmount(String text) {
        if (TextUtils.isEmpty(text)) return 0;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private void updateMoneyUI() {
        mTvPlayerMoney.setText("Số tiền hiện có: " + formatCurrency(GameSession.balance));
        mTvTotalBet.setText("Tổng cược: " + formatCurrency(mTotalBet));
    }
    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }
    private int getRandomNumber(int bound) {
        return new Random().nextInt(bound) + 1;
    }
    private void setControlsEnabled(boolean enabled) {
        mCheckBox1.setEnabled(enabled);
        mCheckBox2.setEnabled(enabled);
        mCheckBox3.setEnabled(enabled);

        mEtBetAmount1.setEnabled(enabled);
        mEtBetAmount2.setEnabled(enabled);
        mEtBetAmount3.setEnabled(enabled);
    }
    private void resetGame() {
        if (mCountDownTimer2 != null) mCountDownTimer2.cancel();
        if (mHandler != null && mRunnable != null) mHandler.removeCallbacks(mRunnable);

        mSeekBar1.setProgress(0);
        mSeekBar2.setProgress(0);
        mSeekBar3.setProgress(0);

        mCheckBox1.setChecked(false);
        mCheckBox2.setChecked(false);
        mCheckBox3.setChecked(false);

        mEtBetAmount1.setText("");
        mEtBetAmount2.setText("");
        mEtBetAmount3.setText("");

        mTotalBet = 0;
        isBetConfirmed = false;
        updateMoneyUI();
        mTvCountDown.setVisibility(View.INVISIBLE);
        setControlsEnabled(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateMoneyUI();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer(mpMusic);
        releaseMediaPlayer(mpButton);
        releaseMediaPlayer(mpCount);
        releaseMediaPlayer(mpError);
        releaseMediaPlayer(mpRacingEffect);
        releaseMediaPlayer(mpWin);
        releaseMediaPlayer(mpLose);
    }
    private void releaseMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
