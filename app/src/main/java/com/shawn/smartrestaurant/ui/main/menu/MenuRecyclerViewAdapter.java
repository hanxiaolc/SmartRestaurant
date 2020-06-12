package com.shawn.smartrestaurant.ui.main.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;


import java.util.List;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Dish> dishList;

    /**
     *
     */
    public static class MenuRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        //
        private View itemView;

        //
        private int itemViewType;

        /**
         *
         */
        MenuRecyclerViewAdapterViewHolder(View itemView, int itemViewType) {
            super(itemView);

            this.itemView = itemView;
            this.itemViewType = itemViewType;
        }
    }

    /**
     *
     */
    MenuRecyclerViewAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     *
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
        View itemViewTemp;

        if (itemViewType == (Code.MenuRecyclerViewType.HEADER.id)) {
            itemViewTemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_group_title_menu, parent, false);
        } else {
            itemViewTemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_menu, parent, false);
        }

        return new MenuRecyclerViewAdapterViewHolder(itemViewTemp, itemViewType);
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Dish dish = this.dishList.get(position);

        if (Code.MenuRecyclerViewType.HEADER.id == holder.getItemViewType()) {
            TextView groupTitle = holder.itemView.findViewById(R.id.textView_menu_group_title);
            groupTitle.setText(dish.getCategory());
        } else {
            TextView dishCode = holder.itemView.findViewById(R.id.textView_menu_dish_code);
            TextView dishName = holder.itemView.findViewById(R.id.textView_menu_dish_name);
            TextView dishPrice = holder.itemView.findViewById(R.id.textView_menu_dish_price);
            dishCode.setText(dish.getDishCode());
            if (null == dish.getDishCode() || dish.getDishCode().isEmpty()) {
                dishCode.setVisibility(View.GONE);
            }
            dishName.setText(dish.getDishName());
            dishPrice.setText("$" + dish.getPrice());

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString(FragmentAddMenu.ARG_DISH, new Gson().toJson(dish));
                Navigation.findNavController(view)
                        .navigate(R.id.action_framelayout_nav_menu_to_framelayout_nav_addmenu, bundle);
            });
        }
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
    @Override
    public int getItemViewType(int position) {
        return null == dishList.get(position).getDishName() ?
                Code.MenuRecyclerViewType.HEADER.id : Code.MenuRecyclerViewType.ITEM.id;
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
