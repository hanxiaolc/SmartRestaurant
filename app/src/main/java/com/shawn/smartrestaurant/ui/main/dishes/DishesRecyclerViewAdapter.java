package com.shawn.smartrestaurant.ui.main.dishes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Dish dish = this.dishList.get(position);

        TextView dishCode = holder.itemView.findViewById(R.id.textView_dishes_code);
        TextView dishName = holder.itemView.findViewById(R.id.textView_dishes_name);
        TextView dishPrice = holder.itemView.findViewById(R.id.textView_dishes_price);
        TextView dishNumber = holder.itemView.findViewById(R.id.textView_dishes_number);
        ImageButton subtractButton = holder.itemView.findViewById(R.id.imageButton_dishes_subtract);
        ImageButton addButton = holder.itemView.findViewById(R.id.imageButton_dishes_add);

        dishCode.setText(dish.getDishCode());
        dishName.setText(dish.getDishName());
        dishPrice.setText("$ " + dish.getPrice());
        dishNumber.setText(String.valueOf(dish.getNumbers()));

        if (null == dish.getDishCode() || dish.getDishCode().isEmpty()) {
            dishCode.setVisibility(View.GONE);
        }

        addButton.setOnClickListener(v -> {
            int number = Integer.parseInt(dishNumber.getText().toString());
            if (99 <= number) {
                return;
            }
            dish.setNumbers(number + 1);
            dishNumber.setText(String.valueOf(dish.getNumbers()));
        });

        subtractButton.setOnClickListener(v -> {
            int number = Integer.parseInt(dishNumber.getText().toString());
            if (0 >= number) {
                return;
            }
            dish.setNumbers(number - 1);
            dishNumber.setText(String.valueOf(dish.getNumbers()));
        });
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