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

        // Race ID
        holder.tvRaceId.setText("Cuộc đua: #" + bet.getRaceId());

        // Amount - hiển thị với format đẹp
        holder.tvAmount.setText("Số tiền: " + formatCurrency(bet.getAmount()));

        // Result với styling theo trạng thái
        String status = bet.getBetStatus();
        holder.tvResult.setText(status);

        // Styling cho status badge
        if ("WIN".equals(status)) {
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.success));
            holder.tvResult.setText("🎉 THẮNG");
        } else {
            holder.tvResult.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
            holder.tvResult.setText("😔 THUA");
        }

        // Winning car vs Selected car
        String carInfo = "Xe thắng: " + bet.getWinningCar();
        if (!bet.getSelectedCar().isEmpty()) {
            carInfo = bet.getSelectedCar().equals(bet.getWinningCar()) ?
                    "Xe thắng: ✅ " + bet.getWinningCar() :
                    "Xe thắng: ❌ " + bet.getWinningCar();
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

    // Method to update the bet list
    public void updateBetList(List<Bet> newBetList) {
        this.betList = newBetList;
        notifyDataSetChanged();
    }
}