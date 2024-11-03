package com.namha.handbagsellingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.namha.handbagsellingapp.R;

import java.util.List;

public class homePagerAdapter extends RecyclerView.Adapter<homePagerAdapter.ViewHolder> {
    private List<Integer> imageList;
    private Context context;
    private OnPageChangeListener pageChangeListener;

    public interface OnPageChangeListener {
        void onPageChanged(int position);
    }

    public homePagerAdapter(Context context, List<Integer> imageList, OnPageChangeListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.pageChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));

        // Thiết lập sự kiện khi click vào item
        holder.itemView.setOnClickListener(v -> {
            if (pageChangeListener != null) {
                pageChangeListener.onPageChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewBanner);
        }
    }
}
