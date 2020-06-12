package com.shawn.smartrestaurant.ui.main.done;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.Objects;


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

        ((MainActivity) requireActivity()).authenticate();

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.table = new Gson().fromJson(getArguments().getString(ARG_TABLE), Table.class);
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

        //
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle(Code.ActionBarTitle.TABLE.value + " " + this.table.getId());

        setHasOptionsMenu(true);

        ((AppBarLayout) requireActivity().findViewById(R.id.appBarLayout_main)).setExpanded(true);

        return inflater.inflate(R.layout.framelayout_nav_order_done, container, false);
    }

    /**
     *
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TableLayout tableLayout = view.findViewById(R.id.tableLayout_order_done);
        TextView totalPriceTextView = view.findViewById(R.id.textView_order_done_total_price);
        Button cashUp = view.findViewById(R.id.button_order_done_cash_up);
        Button cancel = view.findViewById(R.id.button_order_done_cancel);

        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        for (Dish dish : this.table.getDishList()) {
            if (0 != dish.getNumbers()) {
                tableLayout.addView(this.createTableRow(dish));
                tableLayout.addView(this.createTableRowNumber(dish));
            }
        }
        totalPriceTextView.setText(" $" + String.format("%.2f", this.table.getPrice()));

        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(((MainActivity) requireActivity()).getUser().getGroup() + "_" + this.table.getId()).get().addOnSuccessListener(documentSnapshot -> {
            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentOrderDone to check if some tables were changed.");

            if (this.table.getUpdateTime() != Objects.requireNonNull(documentSnapshot.toObject(Table.class)).getUpdateTime()) {
                new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("The status of this table had been changed, please try again after refreshing tables information in table list").setPositiveButton("ok", ((dialog, which) -> {
                    ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
                })).show();
            }
        });

        cashUp.setOnClickListener(v -> {

            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.VISIBLE);

            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(((MainActivity) requireActivity()).getUser().getGroup() + "_" + this.table.getId()).get().addOnSuccessListener(documentSnapshot -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentOrderDone when Cash Up button is clicked to check if the table had been changed.");

                if (this.table.getUpdateTime() != Objects.requireNonNull(documentSnapshot.toObject(Table.class)).getUpdateTime()) {
                    // Release blocking UI and hide progress bar
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("The status of this table had been changed, please try again after refreshing tables information in table list").setPositiveButton("ok", ((dialog, which) -> {
                        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
                    })).show();
                } else {
                    // Save in database as history
                    long currentTime = System.currentTimeMillis();
                    this.table.setEndTime(currentTime);
                    this.table.setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
                    this.table.setUpdateTime(currentTime);

                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_HISTORY).document(String.valueOf(currentTime)).set(this.table);
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Add history in FragmentOrderDone when Cash Up button is clicked.");

                    // Update table status
                    this.table.setStartTime(null);
                    this.table.setPrice(null);
                    this.table.setStatus(Code.TableStatus.STAND_BY.value);
                    for (Dish dish : this.table.getDishList()) {
                        if (0 != dish.getNumbers()) {
                            dish.setNumbers(0);
                        }
                    }
                    ((MainActivity) requireActivity()).getTableMap().put(this.table.getId(), this.table);
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).set(this.table).addOnSuccessListener(aVoid -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentOrderDone when Cash Up button is clicked to set table to be newest.");

                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(table.getGroup()).update(Other.COLUMN_HISTORY_VERSION, currentTime, Other.COLUMN_TABLE_VERSION, currentTime).addOnSuccessListener(otherAVoid -> {
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentOrderDone when Cash Up button is clicked.");

                            // Release blocking UI and hide progress bar
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.GONE);

                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
                        });
                    });
                }
            });
        });

        cancel.setOnClickListener(v -> {
            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.VISIBLE);

            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(((MainActivity) requireActivity()).getUser().getGroup() + "_" + this.table.getId()).get().addOnSuccessListener(documentSnapshot -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentOrderDone when Cancel button is clicked to check if the table had been changed.");

                if (this.table.getUpdateTime() != Objects.requireNonNull(documentSnapshot.toObject(Table.class)).getUpdateTime()) {
                    // Release blocking UI and hide progress bar
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("The status of this table had been changed, please try again after refreshing tables information in table list").setPositiveButton("ok", ((dialog, which) -> {
                        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
                    })).show();
                } else {
                    // Save in database as history
                    long currentTime = System.currentTimeMillis();

                    // Update table status
                    this.table.setStartTime(null);
                    this.table.setPrice(null);
                    this.table.setStatus(Code.TableStatus.STAND_BY.value);
                    this.table.setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
                    this.table.setUpdateTime(currentTime);
                    for (Dish dish : this.table.getDishList()) {
                        if (0 != dish.getNumbers()) {
                            dish.setNumbers(0);
                        }
                    }
                    ((MainActivity) requireActivity()).getTableMap().put(this.table.getId(), this.table);
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).set(this.table).addOnSuccessListener(aVoid -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentOrderDone when Cancel button is clicked to set table to be newest.");

                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(table.getGroup()).update(Other.COLUMN_TABLE_VERSION, currentTime).addOnSuccessListener(otherAVoid -> {
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentOrderDone when Cancel button is clicked.");

                            // Release blocking UI and hide progress bar
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            requireView().findViewById(R.id.progressBar_order_done).setVisibility(View.GONE);

                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
                        });
                    });
                }
            });
        });
    }

    /**
     *
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.option_menu_home, menu);
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.button_menu_home) {
            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_commit_to_fragment_tables, new Bundle());
            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
//        TextView number = new TextView(requireContext());

//        if (10 > index) {
//            indexTextView.setText("0" + index);
//        } else {
//            indexTextView.setText(String.valueOf(index));
//        }
        code.setText(dish.getDishCode());
        name.setText(dish.getDishName());
        price.setText(String.valueOf(dish.getPrice()));
//        number.setText(String.valueOf(dish.getNumbers()));

//        indexTextView.setLayoutParams(params);
//        code.setLayoutParams(params);
//        name.setLayoutParams(params);
//        price.setLayoutParams(params);
//        number.setLayoutParams(params);

//        tableRow.addView(indexTextView);
        tableRow.addView(code);
        tableRow.addView(name);
        tableRow.addView(price);
//        tableRow.addView(number);

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
    public Table getTable() {
        return table;
    }

    /**
     *
     */
    public void setTable(Table table) {
        this.table = table;
    }
}
