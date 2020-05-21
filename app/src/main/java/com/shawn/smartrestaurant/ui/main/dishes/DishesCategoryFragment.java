package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;

import java.util.List;

public class DishesCategoryFragment extends Fragment {

    //
    public static final String ARG_DISH_LIST = "dishList";

    //
    public static final String ARG_DISH_CODE = "dishCode";

    //
    public static final String ARG_DISH_NAME = "dishName";

    //
    public static final String ARG_CATEGORY = "category";

    //
    public static final String ARG_PRICE = "price";

    //
    public static final String ARG_GROUP = "group";

    //
    public static final String ARG_ID = "id";

    //
    public static final String ARG_NUMBERS = "numbers";

    //
    private List dishList;


    /**
     *
     */
    private static DishesCategoryFragment newInstance(String dishList) {
        Bundle args = new Bundle();
        args.putString(ARG_DISH_CODE, dishList);

        DishesCategoryFragment fragment = new DishesCategoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.dishList = new Gson().fromJson(getArguments().getString(ARG_DISH_LIST), List.class);
        }

        return inflater.inflate(R.layout.framelayout_tab_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Find recyclerView
        RecyclerView recyclerView = view.findViewById(R.id.category_recyclerView);

        // Set layoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        // Bind adapter
        DishesRecyclerViewAdapter dishesRecyclerViewAdapter = new DishesRecyclerViewAdapter(this.dishList);
        recyclerView.setAdapter(dishesRecyclerViewAdapter);
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
