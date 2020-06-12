package com.shawn.smartrestaurant.ui.main.history.done;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.ui.main.MainActivity;


/**
 *
 */
public class FragmentHistoryOrderDone extends Fragment {

    //
    public static final String ARG_HISTORY_TABLE = "historyTable";

    //
    private Table historyTable;


    /**
     *
     */
    public FragmentHistoryOrderDone() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param historyTable Parameter historyTable.
     * @return A new instance of fragment framelayout_nav_history_order_done.
     */
    public static FragmentHistoryOrderDone newInstance(String historyTable) {
        Bundle args = new Bundle();
        args.putString(ARG_HISTORY_TABLE, historyTable);

        FragmentHistoryOrderDone fragment = new FragmentHistoryOrderDone();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).authenticate();

        if (getArguments() != null) {
            this.historyTable = new Gson().fromJson(getArguments().getString(ARG_HISTORY_TABLE), Table.class);
        }

        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppBarLayout) requireActivity().findViewById(R.id.appBarLayout_main)).setExpanded(true);

        return inflater.inflate(R.layout.framelayout_nav_history_order_done, container, false);
    }

    /**
     *
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TableLayout tableLayout = view.findViewById(R.id.tableLayout_history_order_done);
        TextView totalPriceTextView = view.findViewById(R.id.textView_history_order_done_total_price);

        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        for (Dish dish : this.historyTable.getDishList()) {
            if (0 != dish.getNumbers()) {
                tableLayout.addView(this.createTableRow(dish));
                tableLayout.addView(this.createTableRowNumber(dish));
            }
        }
        totalPriceTextView.setText(" $" + String.format("%.2f", this.historyTable.getPrice()));
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private TableRow createTableRow(Dish dish) {

        TableRow tableRow = new TableRow(requireContext());
        tableRow.setMinimumHeight(24);

        TextView code = new TextView(requireContext());
        TextView name = new TextView(requireContext());
        TextView price = new TextView(requireContext());

        code.setText(dish.getDishCode());
        name.setText(dish.getDishName());
        price.setText(String.valueOf(dish.getPrice()));

        tableRow.addView(code);
        tableRow.addView(name);
        tableRow.addView(price);

        return tableRow;
    }

    /**
     *
     */
    private TableRow createTableRowNumber(Dish dish) {

        TableRow.LayoutParams layoutParams = new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.column = 2;

        TableRow tableRow = new TableRow(requireContext());
        tableRow.setMinimumHeight(24);

        TextView number = new TextView(requireContext());
        number.setText(String.valueOf(dish.getNumbers()));
        number.setLayoutParams(layoutParams);
        tableRow.addView(number);

        return tableRow;
    }

    /**
     *
     */
    public Table getHistoryTable() {
        return historyTable;
    }

    /**
     *
     */
    public void setHistoryTable(Table historyTable) {
        this.historyTable = historyTable;
    }
}
