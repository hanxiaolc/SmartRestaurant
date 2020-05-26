package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;
import com.shawn.smartrestaurant.ui.main.done.FragmentOrderDone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.grpc.SynchronizationContext;


/**
 *
 */
public class FragmentDishes extends Fragment {

    //
    public static final String ARG_TABLE = "table";

//    //
//    public static final String ARG_TABLE_ID = "tableId";
//
//    //
//    public static final String ARG_TABLE_START_TIME = "tableStartTime";
//
//    //
//    public static final String ARG_TABLE_PRICE = "tablePrice";
//
//    //
//    public static final String ARG_TABLE_STATUS = "tableStatus";
//
//    //
//    public static final String ARG_TABLE_DISH_LIST = "tableDishList";

    //
    private Table table;

//    //
//    private String tableId;
//
//    //
//    private String tableStartTime;
//
//    //
//    private Double tablePrice;
//
//    //
//    private String tableStatus;
//
//    //
//    private List<Dish> tableDishList;
//
//    //
//    private Map<String, List<Dish>> dishCategoryMap;

    //
    private ViewPager2 viewPager;

    /**
     *
     */
    public FragmentDishes() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tableId        Parameter tableId.
     * @param tableStartTime Parameter tableStartTime.
     * @param tablePrice     Parameter tablePrice.
     * @param tableStatus    Parameter tableStatus.
     * @param tableDishList  Parameter tableDishList.
     * @return A new instance of fragment framelayout_nav_dishes.
     */
    public static FragmentDishes newInstance(String tableId, String tableStartTime, Double tablePrice, String tableStatus, List<Dish> tableDishList) {

        Bundle args = new Bundle();

//        args.putString(ARG_TABLE_ID, tableId);
//        args.putString(ARG_TABLE_START_TIME, tableStartTime);
//        args.putDouble(ARG_TABLE_PRICE, tablePrice);
//        args.putString(ARG_TABLE_STATUS, tableStatus);
//        args.putString(ARG_TABLE_DISH_LIST, new Gson().toJson(tableDishList));

        FragmentDishes fragment = new FragmentDishes();
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
        }

        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Map<String, List<Dish>> dishCategoryMap = new HashMap<>();
        for (Dish dish : this.table.getDishList()) {
            if (null == dishCategoryMap.get(dish.getCategory())) {
                List<Dish> tempList = new ArrayList<>();
                tempList.add(dish);
                dishCategoryMap.put(dish.getCategory(), tempList);
            } else {
                Objects.requireNonNull(dishCategoryMap.get(dish.getCategory())).add(dish);
            }
        }

        View view = inflater.inflate(R.layout.framelayout_nav_dishes, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout_dishes);

        DishesTabLayoutAdapter dishesTabLayoutAdapter = new DishesTabLayoutAdapter(this);
        dishesTabLayoutAdapter.setDishCategoryMap(dishCategoryMap);
        this.viewPager = view.findViewById(R.id.viewPager2_dishes);
        this.viewPager.setAdapter(dishesTabLayoutAdapter);

        List<String> keyList = new ArrayList<>(dishCategoryMap.keySet());
        Collections.sort(keyList);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(keyList.get(position));
        }
        ).attach();

        setHasOptionsMenu(true);

        return view;
    }

    /**
     *
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.option_menu_dishes_commit, menu);
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_dishes_order_done) {
            DishesTabLayoutAdapter adapter = (DishesTabLayoutAdapter) this.viewPager.getAdapter();

            List<Dish> dishList = new ArrayList<>();
            for (Map.Entry<String, List<Dish>> entry : Objects.requireNonNull(adapter).getDishCategoryMap().entrySet()) {
                dishList.addAll(entry.getValue());
            }

            boolean ordered = false;
            double totalPrice = 0;
            for (Dish dish : dishList) {
                if (0 < dish.getNumbers()) {
                    ordered = true;
                    totalPrice = totalPrice + (dish.getNumbers() * dish.getPrice());
                }
            }
            if (!ordered) {
                Toast.makeText(requireContext(), "Warning: No dish is ordered.", Toast.LENGTH_SHORT).show();
                return false;
            }

            Table table = ((MainActivity) requireActivity()).getTableMap().get(this.table.getId());
            long currentTime = System.currentTimeMillis();
            Objects.requireNonNull(table).setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
            table.setUpdateTime(currentTime);
            table.setStatus(Code.TableStatus.ON_SERVICE.value);
            table.setDishList(dishList);
            table.setStartTime(currentTime);
            table.setPrice(totalPrice);

            Bundle bundle = new Bundle();
            bundle.putString(FragmentOrderDone.ARG_TABLE, new Gson().toJson(table));
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).update(Table.COLUMN_UPDATE_TIME, System.currentTimeMillis(), Table.COLUMN_UPDATE_USER, ((MainActivity) requireActivity()).getUser().getId(), Table.COLUMN_STATUS, Code.TableStatus.ON_SERVICE.value, Table.COLUMN_START_TIME, currentTime, Table.COLUMN_PRICE, totalPrice, Table.COLUMN_DISH_LIST, dishList);
            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_dishes_to_fragment_commit, bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
