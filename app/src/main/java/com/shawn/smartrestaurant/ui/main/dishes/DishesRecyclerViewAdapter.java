package com.shawn.smartrestaurant.ui.main.dishes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;

import java.util.List;

public class DishesRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Dish> dishList;

    /**
     *
     */
    DishesRecyclerViewAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     *
     */
    public static class DishesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        //
        View view;

        /**
         *
         */
        DishesRecyclerViewAdapterViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    /**
     *
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dishesItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_dishes, parent, false);
        return new DishesRecyclerViewAdapterViewHolder(dishesItemView);
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Dish dish = this.dishList.get(position);

        TextView textView = holder.itemView.findViewById(R.id.dishes_itemView_textView);
        ImageView imageView = holder.itemView.findViewById(R.id.dishes_itemView_imageView);
        textView.setText(dish.getDishName());
        imageView.setImageResource(R.drawable.ic_people_black_24dp);
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return dishList.size();
    }

    /**
     *
     */
    public List<Dish> getDishList() {
        return dishList;
    }

    /**
     *
     */
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }
}