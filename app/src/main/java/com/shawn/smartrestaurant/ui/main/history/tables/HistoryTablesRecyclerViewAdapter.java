package com.shawn.smartrestaurant.ui.main.history.tables;

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
import com.shawn.smartrestaurant.ui.main.history.done.FragmentHistoryOrderDone;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 */
public class HistoryTablesRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Table> historyTableList;

    /**
     *
     */
    HistoryTablesRecyclerViewAdapter(List<Table> historyTableList) {
        this.historyTableList = historyTableList;
    }

    /**
     *
     */
    public static class HistoryTablesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        View view;

        HistoryTablesRecyclerViewAdapterViewHolder(View view) {
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
        View historyTablesItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_history_tables, parent, false);
        return new HistoryTablesRecyclerViewAdapterViewHolder(historyTablesItemView);
    }

    /**
     *
     */
    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Table historyTable = this.historyTableList.get(position);

        TextView id = holder.itemView.findViewById(R.id.textView_history_table_id);
        TextView date = holder.itemView.findViewById(R.id.textView_history_table_date);
        TextView startTime = holder.itemView.findViewById(R.id.textView_history_table_start_time);
        TextView endTime = holder.itemView.findViewById(R.id.textView_history_table_end_time);
        TextView price = holder.itemView.findViewById(R.id.textView_history_table_price);

        id.setText(historyTable.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        date.setText(new SimpleDateFormat("MM/dd/yyyy").format(historyTable.getEndTime()));
        startTime.setText(sdf.format(historyTable.getStartTime()));
        endTime.setText(sdf.format(historyTable.getEndTime()));
        price.setText("$" + String.format("%.2f", historyTable.getPrice()));

        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentHistoryOrderDone.ARG_HISTORY_TABLE, new Gson().toJson(historyTable));
            Navigation.findNavController(view)
                    .navigate(R.id.action_framelayout_nav_history_tables_to_framelayout_nav_history_order_done, bundle);
        });
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return historyTableList.size();
    }

    /**
     *
     */
    public List<Table> getHistoryTableList() {
        return historyTableList;
    }

    /**
     *
     */
    public void setHistoryTableList(List<Table> historyTableList) {
        this.historyTableList = historyTableList;
    }
}
