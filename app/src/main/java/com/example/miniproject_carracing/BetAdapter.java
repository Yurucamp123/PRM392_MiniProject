package com.example.miniproject_carracing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Bet;

public class BetAdapter extends RecyclerView.Adapter<BetAdapter.BetViewHolder> {

    private List<Bet> betList;

    public BetAdapter(List<Bet> betList) {
        this.betList = betList;
    }

    @NonNull
    @Override
    public BetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bet, parent, false);
        return new BetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BetViewHolder holder, int position) {
        Bet bet = betList.get(position);
        holder.tvRaceId.setText("Cuộc đua: #" + bet.getRaceId());
        holder.tvAmount.setText("Số tiền: " + formatCurrency(bet.getAmount()));
        String status = bet.getBetStatus();
        holder.tvResult.setText(status);
        if ("WIN".equals(status)) {
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.success));
            holder.tvResult.setText("🎉 THẮNG");
        } else if ("LOSE".equals(status)) {
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
            holder.tvResult.setText("😔 THUA");
        } else {
            if (status != null && status.contains("THẮNG")) {
                holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.success));
                holder.tvResult.setText("🎉 THẮNG");
            } else {
                holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
                holder.tvResult.setText("😔 THUA");
            }
        }

        String selectedCar = bet.getSelectedCar();
        String winningCar = bet.getWinningCar();

        String carInfo;
        if (selectedCar != null && !selectedCar.isEmpty()) {
            boolean isWin = selectedCar.equals(winningCar);
            carInfo = String.format("Xe chọn: %s %s\nXe thắng: %s",
                    selectedCar,
                    isWin ? "✅" : "❌",
                    winningCar);
        } else {
            carInfo = "Xe thắng: " + winningCar;
        }
        holder.tvWinningCar.setText(carInfo);

        // Date time
        holder.tvDateTime.setText("Thời gian: " + bet.getDateTime());
    }

    @Override
    public int getItemCount() {
        return betList.size();
    }

    public static class BetViewHolder extends RecyclerView.ViewHolder {
        TextView tvRaceId, tvAmount, tvResult, tvDateTime, tvWinningCar;

        public BetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRaceId = itemView.findViewById(R.id.tvRaceId);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvResult = itemView.findViewById(R.id.tvResult);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvWinningCar = itemView.findViewById(R.id.tvWinningCar);
        }
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }
    public void updateBetList(List<Bet> newBetList) {
        this.betList = newBetList;
        notifyDataSetChanged();
    }
}