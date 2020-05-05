package com.shawn.smartrestaurant.ui.main.addmenu;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.ui.login.LoginActivity;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddMenu extends Fragment {

    //
    private ArrayAdapter<String> adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAddMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddMenu newInstance(String param1, String param2) {
        FragmentAddMenu fragment = new FragmentAddMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_addmenu, container, false);
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the elements of fragment
        ImageView dishImage = view.findViewById(R.id.imageView_dish_image);
        TextInputEditText dishName = view.findViewById(R.id.editText_add_menu_dish_name);
        AutoCompleteTextView category = view.findViewById(R.id.autoCompleteTextView_add_menu_category);
        TextInputEditText price = view.findViewById(R.id.editText_add_menu_price);
        Button addToMenu = view.findViewById(R.id.button_add_to_menu);
        ImageButton fetchFromAbulm = view.findViewById(R.id.button_fetch_from_album);
        ImageButton takePhoto = view.findViewById(R.id.button_take_photo);

        // Prepare price dropdown list
        if (null == this.adapter) {
            this.adapter =
                    new ArrayAdapter<>(
                            requireContext(),
                            R.layout.item_list_menu_category,
                            new String[]{"Dish", "Main Food", "Dessert", "Drink", "Flavor"});
        }
        category.setAdapter(this.adapter);

        // Set button behavior
        addToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(dishName.getText()).toString().isEmpty() || category.getText().toString().isEmpty() || Objects.requireNonNull(price.getText()).toString().isEmpty()) {
                    alertDisplay("Failed", "Dish Name, Category and Price should not be empty.", (dialog, which) -> {
                    });
                    return;
                }
            }
        });

        fetchFromAbulm.setOnClickListener(v -> {
            String s = null;
            String s1 = null;
        });

        takePhoto.setOnClickListener(v -> {
            String s = null;
            String s1 = null;
        });

    }

    /**
     *
     */
    private void alertDisplay(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener);
        AlertDialog alertDialogButton = builder.create();
        alertDialogButton.show();
    }
}
