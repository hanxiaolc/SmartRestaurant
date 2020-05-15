package com.shawn.smartrestaurant.ui.main.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;

import java.util.ArrayList;
import java.util.List;

public class FragmentSetting extends Fragment {

    //
    AutoCompleteTextView numberOfTables;

    //
    ArrayAdapter<Integer> adapter;

    /**
     *
     */
    private static FragmentAddMenu newInstance(byte[] imageView, String dishCode, String dishName, String category, String price, boolean hasImage, String action) {
        FragmentAddMenu fragment = new FragmentAddMenu();
        Bundle args = new Bundle();
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
//            this.imageView = getArguments().getByteArray(ARG_IMAGE_VIEW);
//            this.dishCode = getArguments().getString(ARG_DISH_CODE);
//            this.dishName = getArguments().getString(ARG_DISH_NAME);
//            this.category = getArguments().getString(ARG_CATEGORY);
//            this.price = getArguments().getDouble(ARG_PRICE);
//            this.hasImage = getArguments().getBoolean(ARG_HAS_IMAGE);
//            this.action = getArguments().getString(ARG_ACTION);
        }
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        // TODO
        this.numberOfTables.setText(String.valueOf(1), false);

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
                List<Table> tables = new ArrayList<>();
                if (this.before != s) {
                    // TODO Add onFailureListener
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, (((MainActivity) requireActivity()).getUser().getGroup())).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            Table tempTable = ds.toObject(Table.class);

                            // TODO Add onFailureListener
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(tempTable.getGroup() + "_" + tempTable.getId()).delete();
                        }

                        for (int i = 0; i < Integer.parseInt(String.valueOf(s)); i++) {
                            Table table = new Table();
                            table.setId(String.valueOf(i + 1));
                            table.setGroup(((MainActivity) requireActivity()).getUser().getGroup());
                            table.setDishList(((MainActivity) requireActivity()).getDishList());

                            // TODO Add onFailureListener
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).set(table);
                        }
                    });
                }
            }

            /**
             *
             */
            @Override
            public void afterTextChanged(Editable s) {
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
