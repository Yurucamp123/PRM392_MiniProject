package com.example.miniproject_carracing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RacingActivity extends AppCompatActivity implements OnListenerClickCheckbox {

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
    private int mPlayerMoney = 100000; // Tiền người chơi ban đầu
    private int mTotalBet = 0;
    private boolean isBetConfirmed = false; // Kiểm tra đã đặt cược

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_racing);

        // Ánh xạ view
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

        mDialog = new Dialog(this);

        // Dialog setup
        mDialog.setContentView(R.layout.alert_winner_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;

        mButtonPlayAgain = mDialog.findViewById(R.id.btn_playAgain);
        mButtonExit = mDialog.findViewById(R.id.btn_exit);
        mTvResult = mDialog.findViewById(R.id.tv_result);
        mResultImage = mDialog.findViewById(R.id.img_result);

        mHandler = new Handler();

        // Giới hạn checkbox tối đa chọn 2 xe
        CompoundButton.OnCheckedChangeListener checkLimitListener = (buttonView, isChecked) -> {
            int countChecked = 0;
            if (mCheckBox1.isChecked()) countChecked++;
            if (mCheckBox2.isChecked()) countChecked++;
            if (mCheckBox3.isChecked()) countChecked++;
            if (countChecked > 2) {
                buttonView.setChecked(false);
                Toast.makeText(this, "Chỉ được cược tối đa 2 xe", Toast.LENGTH_SHORT).show();
            }
        };
        mCheckBox1.setOnCheckedChangeListener(checkLimitListener);
        mCheckBox2.setOnCheckedChangeListener(checkLimitListener);
        mCheckBox3.setOnCheckedChangeListener(checkLimitListener);

        Button btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            finish(); // hoặc nếu bạn có Activity chính thì dùng Intent quay lại đó
        });

        // Nút Start
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            if (!isBetConfirmed) {
                Toast.makeText(this, "Vui lòng xác nhận đặt cược trước khi bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }
            startPlaying();
        });

        // Nút Reset
        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> resetGame());

        // Nút Đặt cược
        Button btnBet = findViewById(R.id.btn_bet);
        btnBet.setOnClickListener(v -> showConfirmBetDialog());

        setupDialogButtons();

        updateMoneyUI();
    }

    private void showConfirmBetDialog() {
        // Kiểm tra xe được chọn
        if (!mCheckBox1.isChecked() && !mCheckBox2.isChecked() && !mCheckBox3.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 xe để đặt cược", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy tiền cược từng xe
        int bet1 = parseBetAmount(mEtBetAmount1.getText().toString());
        int bet2 = parseBetAmount(mEtBetAmount2.getText().toString());
        int bet3 = parseBetAmount(mEtBetAmount3.getText().toString());

        // Xe không được chọn mà có cược báo lỗi
        if ((bet1 > 0 && !mCheckBox1.isChecked())
                || (bet2 > 0 && !mCheckBox2.isChecked())
                || (bet3 > 0 && !mCheckBox3.isChecked())) {
            Toast.makeText(this, "Xe không được chọn không được đặt cược", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tổng tiền cược
        int totalBetTemp = bet1 + bet2 + bet3;
        if (totalBetTemp == 0) {
            Toast.makeText(this, "Vui lòng nhập số tiền cược", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBetTemp > mPlayerMoney) {
            Toast.makeText(this, "Số tiền cược vượt quá số tiền hiện có", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đặt cược")
                .setMessage("Bạn có muốn đặt cược tổng cộng " + totalBetTemp + " không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    // Cập nhật tổng cược, trạng thái cược đã xác nhận
                    mTotalBet = totalBetTemp;
                    isBetConfirmed = true;
                    Toast.makeText(RacingActivity.this, "Đặt cược thành công", Toast.LENGTH_SHORT).show();

                    // Khóa checkbox + edittext
                    setControlsEnabled(false);

                    updateMoneyUI();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void startPlaying() {
        // Reset progress
        mSeekBar1.setProgress(0);
        mSeekBar2.setProgress(0);
        mSeekBar3.setProgress(0);

        mCountDownNumber = 4;
        mTvCountDown.setVisibility(View.VISIBLE);

        mCountDownTimer2 = new CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")
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
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeekBar1.setProgress(mSeekBar1.getProgress() + getRandomNumber(3));
                mSeekBar2.setProgress(mSeekBar2.getProgress() + getRandomNumber(3));
                mSeekBar3.setProgress(mSeekBar3.getProgress() + getRandomNumber(3));

                boolean finished = mSeekBar1.getProgress() >= 100 || mSeekBar2.getProgress() >= 100 || mSeekBar3.getProgress() >= 100;

                if (finished) {
                    mHandler.removeCallbacks(this);

                    // Cho phép tương tác lại
                    setControlsEnabled(true);

                    // Xác định xe thắng
                    int winner = 0;
                    int maxProgress = Math.max(mSeekBar1.getProgress(), Math.max(mSeekBar2.getProgress(), mSeekBar3.getProgress()));
                    if (mSeekBar1.getProgress() == maxProgress && maxProgress >= 100) winner = 1;
                    else if (mSeekBar2.getProgress() == maxProgress && maxProgress >= 100) winner = 2;
                    else if (mSeekBar3.getProgress() == maxProgress && maxProgress >= 100) winner = 3;

                    // Kiểm tra người chơi thắng
                    boolean playerWin = false;
                    int winAmount = 0;

                    if (winner == 1 && mCheckBox1.isChecked()) {
                        playerWin = true;
                        winAmount = parseBetAmount(mEtBetAmount1.getText().toString()) * 2;
                    } else if (winner == 2 && mCheckBox2.isChecked()) {
                        playerWin = true;
                        winAmount = parseBetAmount(mEtBetAmount2.getText().toString()) * 2;
                    } else if (winner == 3 && mCheckBox3.isChecked()) {
                        playerWin = true;
                        winAmount = parseBetAmount(mEtBetAmount3.getText().toString()) * 2;
                    }

                    if (playerWin) {
                        mPlayerMoney = mPlayerMoney - mTotalBet + winAmount;
                        mResultImage.setImageResource(R.drawable.ic_winner_cup);
                        mTvResult.setText("You Win!!!");
                    } else {
                        mPlayerMoney = mPlayerMoney - mTotalBet;
                        mResultImage.setImageResource(R.drawable.ic_game_over);
                        mTvResult.setText("You Lost!!!");
                    }

                    updateMoneyUI();

                    // Reset trạng thái cược
                    isBetConfirmed = false;
                    mTotalBet = 0;

                    // Hiển thị dialog kết quả
                    mDialog.show();

                } else {
                    mHandler.postDelayed(this, 100);
                }
            }
        };

        mHandler.post(mRunnable);
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
        mTvPlayerMoney.setText("Số tiền hiện có: " + mPlayerMoney);
        mTvTotalBet.setText("Tổng cược: " + mTotalBet);
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

        if (mDialog.isShowing()) mDialog.dismiss();
    }

    private void setupDialogButtons() {
        mButtonPlayAgain.setOnClickListener(v -> {
            mDialog.dismiss();
            resetGame();
        });

        mButtonExit.setOnClickListener(v -> finish());
    }

    @Override
    public void OnCheckBoxItemCheck(int position) {
        // Không dùng
    }
}
