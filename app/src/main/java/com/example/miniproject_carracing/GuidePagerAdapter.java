package com.example.miniproject_carracing;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GuidePagerAdapter extends RecyclerView.Adapter<GuidePagerAdapter.GuideViewHolder> {

    private List<GuideActivity.GuideItem> guideItems;

    public GuidePagerAdapter(List<GuideActivity.GuideItem> guideItems) {
        this.guideItems = guideItems;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guide_page, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        GuideActivity.GuideItem item = guideItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return guideItems.size();
    }

    static class GuideViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvSubtitle;
        private TextView tvContent;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvContent = itemView.findViewById(R.id.tvContent);
        }

        public void bind(GuideActivity.GuideItem item) {
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSubtitle());
            tvContent.setText(item.getContent());
        }
    }
}
