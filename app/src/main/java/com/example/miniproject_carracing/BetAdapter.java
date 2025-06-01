package com.example.miniproject_carracing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import model.Bet;


public class BetAdapter extends RecyclerView.Adapter<BetAdapter.BetViewHolder> {
    private List<Bet> betList;

    public BetAdapter(List<Bet> betList) {
        this.betList = betList;
    }

    public static class BetViewHolder extends RecyclerView.ViewHolder {
        TextView tvRace, tvAmount, tvResult;

        public BetViewHolder(View itemView) {
            super(itemView);
            tvRace = itemView.findViewById(R.id.tvRace);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvResult = itemView.findViewById(R.id.tvResult);
        }
    }

    @Override
    public BetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bet, parent, false);
        return new BetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BetViewHolder holder, int position) {
        Bet bet = betList.get(position);
        holder.tvRace.setText("Race #" + bet.getRaceId());
        holder.tvAmount.setText("Bet: $" + bet.getAmount());
        holder.tvResult.setText("Result: " + bet.getResult());
    }

    @Override
    public int getItemCount() {
        return betList.size();
    }
}
