package com.shawn.smartrestaurant.ui.main.tables;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.shawn.smartrestaurant.R;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter {

    private String[] mDataset = {"hanxiao1", "hanxiao2", "hanxiao3", "hanxiao4", "hanxiao5", "hanxiao1", "hanxiao2", "hanxiao3", "hanxiao4", "hanxiao5", "hanxiao1", "hanxiao2", "hanxiao3", "hanxiao4", "hanxiao5"};

    public static class TablesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        View view;

        TablesRecyclerViewAdapterViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tablesItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_tables, parent, false);
        return new TablesRecyclerViewAdapterViewHolder(tablesItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String item = mDataset[position];
        TextView textView = holder.itemView.findViewById(R.id.tables_itemView_textView);
        ImageView imageView = holder.itemView.findViewById(R.id.tables_itemView_imageView);
        textView.setText(item);
        imageView.setImageResource(R.drawable.ic_people_black_24dp);
        holder.itemView.setOnClickListener(view -> {
            String text = ((TextView) view.findViewById(R.id.tables_itemView_textView)).getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("amount", text);
            Navigation.findNavController(view)
                    .navigate(R.id.action_fragment_tables_to_fragment_dishes, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
