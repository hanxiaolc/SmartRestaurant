package com.shawn.smartrestaurant.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawn.smartrestaurant.R;

public class DishesRecyclerViewAdapter extends RecyclerView.Adapter {

    private String[] mDataset = {"hanxiaolc1", "hanxiaolc2", "hanxiaolc3", "hanxiaolc4", "hanxiaolc5", "hanxiaolc1", "hanxiaolc2", "hanxiaolc3", "hanxiaolc4", "hanxiaolc5", "hanxiaolc1", "hanxiaolc2", "hanxiaolc3", "hanxiaolc4", "hanxiaolc5"};

    public static class DishesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public View view;

        DishesRecyclerViewAdapterViewHolder(View view) {
            super(view);
            //this.view = view;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dishesItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_dishes, parent, false);
        return new DishesRecyclerViewAdapterViewHolder(dishesItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String item = mDataset[position];
        TextView textView = holder.itemView.findViewById(R.id.dishes_itemView_textView);
        ImageView imageView = holder.itemView.findViewById(R.id.dishes_itemView_imageView);
        textView.setText(item);
        imageView.setImageResource(R.drawable.ic_people_black_24dp);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text = ((TextView) view.findViewById(R.id.dishes_itemView_textView)).getText().toString();
//                Bundle bundle = new Bundle();
//                bundle.putString("amount", text);
//                Navigation.findNavController(view)
//                        .navigate(R.id.action_fragment_tables_to_fragment_dishes, bundle);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
