package com.shawn.smartrestaurant.ui.main.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class FragmentSetting extends Fragment {

    //
    private AutoCompleteTextView numberOfTables;

    //
    private ArrayAdapter<Integer> adapter;

//    /**
//     *
//     */
//    private static FragmentAddMenu newInstance(byte[] imageView, String dishCode, String dishName, String category, String price, boolean hasImage, String action) {
//        FragmentAddMenu fragment = new FragmentAddMenu();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).authenticate();
//        if (getArguments() != null) {
//            this.imageView = getArguments().getByteArray(ARG_IMAGE_VIEW);
//            this.dishCode = getArguments().getString(ARG_DISH_CODE);
//            this.dishName = getArguments().getString(ARG_DISH_NAME);
//            this.category = getArguments().getString(ARG_CATEGORY);
//            this.price = getArguments().getDouble(ARG_PRICE);
//            this.hasImage = getArguments().getBoolean(ARG_HAS_IMAGE);
//            this.action = getArguments().getString(ARG_ACTION);
//        }
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppBarLayout) requireActivity().findViewById(R.id.appBarLayout_main)).setExpanded(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_setting, container, false);
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (null == this.numberOfTables) {
            this.numberOfTables = view.findViewById(R.id.autoCompleteTextView_setting_number_of_tables);
        }

        setUpNumberOfTables();

        if (!((MainActivity) requireActivity()).getUser().isManager()) {
            this.numberOfTables.setFocusable(false);
            this.numberOfTables.setAdapter(null);
        }
    }

    /**
     *
     */
    private void setUpNumberOfTables() {

        // Prepare number of tables dropdown list
        if (null == this.adapter) {
            Integer[] intArray = new Integer[Code.MAX_NUMBER_OF_TABLES];

            for (int i = 0; i < Code.MAX_NUMBER_OF_TABLES; i++) {
                intArray[i] = i + 1;
            }

            this.adapter =
                    new ArrayAdapter<>(
                            requireContext(),
                            R.layout.item_list_setting_number_of_tables,
                            intArray);
        }

        this.numberOfTables.setAdapter(this.adapter);

        // TODO Add onFailureListener
        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentSetting to set the default table number.");

            this.numberOfTables.setText(String.valueOf(queryDocumentSnapshots.getDocuments().size()), false);

        });

        this.numberOfTables.addTextChangedListener(new TextWatcher() {

            //
            CharSequence before;

            /**
             *
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.before = s;
            }

            /**
             *
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            /**
             *
             */
            @Override
            public void afterTextChanged(Editable s) {

//                if (this.before.equals(s)) {
//                    // Release blocking UI and hide progress bar
//                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    requireView().findViewById(R.id.progressBar_setting).setVisibility(View.GONE);
//                    return;
//                }

                AtomicBoolean isOnService = new AtomicBoolean(false);
                List<Table> tables = new ArrayList<>();
                if (!this.before.toString().isEmpty() && !this.before.equals(s)) {

                    // Block UI and show progress bar
                    requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    requireView().findViewById(R.id.progressBar_setting).setVisibility(View.VISIBLE);

                    // TODO Add onFailureListener
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, (((MainActivity) requireActivity()).getUser().getGroup())).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables in FragmentSetting when table numbers were changed.");

                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            Table tempTable = ds.toObject(Table.class);
                            if (Code.TableStatus.ON_SERVICE.value.equals(Objects.requireNonNull(tempTable).getStatus())) {
                                isOnService.set(true);
                            }
                            tables.add(tempTable);
                        }

                        if (isOnService.get()) {
                            // Release blocking UI and hide progress bar
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            requireView().findViewById(R.id.progressBar_setting).setVisibility(View.GONE);

                            new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Menu information could not be fixed if even single table in ON SERVICE condition.").setPositiveButton("OK", ((dialog, which) -> {
                            })).show();

                            return;
                        }

                        for (Table table : tables) {
                            // TODO Add onFailureListener
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(Objects.requireNonNull(table).getGroup() + "_" + table.getId()).delete();
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Delete tables in FragmentSetting when table numbers were changed.");

                        }

                        for (int i = 0; i < Integer.parseInt(String.valueOf(s)); i++) {
                            Table table = new Table();
                            if (10 > (i + 1)) {
                                table.setId("0" + (i + 1));
                            } else {
                                table.setId(String.valueOf(i + 1));
                            }

                            table.setGroup(((MainActivity) requireActivity()).getUser().getGroup());
                            table.setDishList(((MainActivity) requireActivity()).getDishList());
                            table.setStatus(Code.TableStatus.STAND_BY.value);
                            table.setCreateTime(System.currentTimeMillis());
                            table.setUpdateTime(System.currentTimeMillis());
                            table.setCreateUser(((MainActivity) requireActivity()).getUser().getId());
                            table.setUpdateUser(((MainActivity) requireActivity()).getUser().getId());

                            // TODO Add onFailureListener
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).set(table);
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set tables in FragmentSetting when table numbers were changed.");
                        }

                        // TODO Add onFailureListener
                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_TABLE_VERSION, System.currentTimeMillis()).addOnSuccessListener(aVoid -> {
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentSetting when table numbers were changed.");

                            // Release blocking UI and hide progress bar
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            requireView().findViewById(R.id.progressBar_setting).setVisibility(View.GONE);

                            new MaterialAlertDialogBuilder(requireContext()).setTitle("Successful").setMessage("Menu information have been updated.").setPositiveButton("OK", (dialog, which) -> {
                            }).show();
                        });
                    });
                }
            }
        });
    }

    /**
     *
     */
    public ArrayAdapter<Integer> getAdapter() {
        return adapter;
    }

    /**
     *
     */
    public void setAdapter(ArrayAdapter<Integer> adapter) {
        this.adapter = adapter;
    }
}
