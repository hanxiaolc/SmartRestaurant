package com.shawn.smartrestaurant.ui.main.tables;

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
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.ui.main.dishes.FragmentDishes;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 */
public class TablesRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Table> tableList;

    /**
     *
     */
    TablesRecyclerViewAdapter(List<Table> tableList) {
        this.tableList = tableList;
    }

    /**
     *
     */
    public static class TablesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        View view;

        TablesRecyclerViewAdapterViewHolder(View view) {
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
        View tablesItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_tables, parent, false);
        return new TablesRecyclerViewAdapterViewHolder(tablesItemView);
    }

    /**
     *
     */
    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Table table = this.tableList.get(position);

        TextView id = holder.itemView.findViewById(R.id.textView_table_id);
        View timeLayout = holder.itemView.findViewById(R.id.linearLayout_table_time);
        View priceLayout = holder.itemView.findViewById(R.id.linearLayout_table_price);
        TextView startTime = holder.itemView.findViewById(R.id.textView_table_start_time);
        TextView price = holder.itemView.findViewById(R.id.textView_table_price);
        TextView status = holder.itemView.findViewById(R.id.textView_table_status);

        id.setText(table.getId());
        status.setText(table.getStatus());

        if (null == table.getStartTime() && null == table.getPrice()) {
            timeLayout.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
        } else {
            timeLayout.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            startTime.setText("　" + new SimpleDateFormat("HH:mm").format(table.getStartTime()));
            price.setText("　$" + String.format("%.2f", table.getPrice()));
        }

        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentDishes.ARG_TABLE, new Gson().toJson(table));
//
//            bundle.putString(FragmentDishes.ARG_TABLE_ID, table.getId());
//            bundle.putString(FragmentDishes.ARG_TABLE_STATUS, table.getStatus());
//            bundle.putString(FragmentDishes.ARG_TABLE_DISH_LIST, new Gson().toJson(table.getDishList()));
//            if (null != table.getStartTime()) {
//                bundle.putLong(FragmentDishes.ARG_TABLE_START_TIME, table.getStartTime().getTime());
//            }
            if (null != table.getPrice()) {
//                bundle.putDouble(FragmentDishes.ARG_TABLE_PRICE, table.getPrice());
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_tables_to_fragment_commit, bundle);
            } else {
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_tables_to_fragment_dishes, bundle);
            }
        });
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return tableList.size();
    }

    /**
     *
     */
    public List<Table> getTableList() {
        return tableList;
    }

    /**
     *
     */
    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }
}
