package com.example.miniproject_carracing;

import android.view.*;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvAmount, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvTransactionType);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvTime = itemView.findViewById(R.id.tvTransactionTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction t = transactions.get(position);
        holder.tvType.setText(t.getType());
        holder.tvAmount.setText(formatCurrency(t.getAmount()));
        holder.tvTime.setText(t.getDateTime());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount).replace(',', '.') + " VND";
    }
}
