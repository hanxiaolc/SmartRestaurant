package com.shawn.smartrestaurant.ui.main.done;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.ui.main.MainActivity;


/**
 *
 */
public class FragmentOrderDone extends Fragment {

    //
    public static final String ARG_TABLE = "table";

    //
    private Table table;


    /**
     *
     */
    public FragmentOrderDone() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param table Parameter table.
     * @return A new instance of fragment framelayout_nav_order_done.
     */
    public static FragmentOrderDone newInstance(String table) {
        Bundle args = new Bundle();
        args.putString(ARG_TABLE, table);

        FragmentOrderDone fragment = new FragmentOrderDone();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.table = new Gson().fromJson(getArguments().getString(ARG_TABLE), Table.class);

            ((MainActivity) requireActivity()).getTableMap().put(this.table.getId(), this.table);
            ((MainActivity) requireActivity()).setCurrentFragment(this);
        }
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.framelayout_nav_order_done, container, false);
    }

    /**
     *
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TableLayout tableLayout = view.findViewById(R.id.tableLayout_order_done);
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        TextView totalPriceTextView = view.findViewById(R.id.textView_order_done_total_price);

        double totalPrice = 0;
        for (Dish dish : this.table.getDishList()) {
            if (0 != dish.getNumbers()) {
                tableLayout.addView(this.createTableRow(dish));
                totalPrice = totalPrice + (dish.getPrice() * dish.getNumbers());
            }
        }
        totalPriceTextView.setText(" $" + String.format("%.2f", totalPrice));
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private TableRow createTableRow(Dish dish/**, int index*/) {
        TableRow tableRow = new TableRow(requireContext());
        tableRow.setMinimumHeight(24);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMarginEnd(8);

//        TextView indexTextView = new TextView(requireContext());
        TextView code = new TextView(requireContext());
        TextView name = new TextView(requireContext());
        TextView price = new TextView(requireContext());
        TextView number = new TextView(requireContext());

//        if (10 > index) {
//            indexTextView.setText("0" + index);
//        } else {
//            indexTextView.setText(String.valueOf(index));
//        }
        code.setText(dish.getDishCode());
        name.setText(dish.getDishName());
        price.setText(String.valueOf(dish.getPrice()));
        number.setText(String.valueOf(dish.getNumbers()));

//        indexTextView.setLayoutParams(params);
//        code.setLayoutParams(params);
//        name.setLayoutParams(params);
//        price.setLayoutParams(params);
//        number.setLayoutParams(params);

//        tableRow.addView(indexTextView);
        tableRow.addView(code);
        tableRow.addView(name);
        tableRow.addView(price);
        tableRow.addView(number);


        return tableRow;
    }
}
