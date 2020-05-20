package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDishes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDishes extends Fragment {

    RecyclerView dishesRecyclerView;

    public static final String ARG_TABLE_ID = "tableId";
    public static final String ARG_TABLE_START_TIME = "tableStartTime";
    public static final String ARG_TABLE_PRICE = "tablePrice";
    public static final String ARG_TABLE_STATUS = "tableStatus";
    public static final String ARG_TABLE_DISHLIST = "tableDishList";

    private String tableId;
    private String tableStartTime;
    private Double tablePrice;
    private String tableStatus;
    private List<Dish> tableDishList;

    public FragmentDishes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tableId        Parameter tableId.
     * @param tableStartTime Parameter tableStartTime.
     * @param tablePrice     Parameter tablePrice.
     * @param tableStatus    Parameter tableStatus.
     * @return A new instance of fragment framelayout_nav_dishes.
     */
    public static FragmentDishes newInstance(String tableId, String tableStartTime, Double tablePrice, String tableStatus) {
        FragmentDishes fragment = new FragmentDishes();
        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);
        args.putString(ARG_TABLE_START_TIME, tableStartTime);
        args.putDouble(ARG_TABLE_PRICE, tablePrice);
        args.putString(ARG_TABLE_STATUS, tableStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tableId = getArguments().getString(ARG_TABLE_ID);
            this.tableStartTime = getArguments().getString(ARG_TABLE_START_TIME);
            this.tablePrice = getArguments().getDouble(ARG_TABLE_PRICE);
            this.tableStatus = getArguments().getString(ARG_TABLE_STATUS);
        }

        ((MainActivity) getActivity()).getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) getActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) getActivity()).setCurrentFragment(this);
//        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        ActionBarDrawerToggle.Delegate delegate = ((MainActivity) getActivity()).getDrawerToggleDelegate();
//        Objects.requireNonNull(delegate).setActionBarUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp, null), R.string.nav_app_bar_navigate_up_description);

//        ActionBar actionbar = ((MainActivity) getActivity()).getSupportActionBar();
//        NavController navController = ((NavHostFragment) this.getParentFragment()).getNavController();
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                .setDrawerLayout(((MainActivity) getActivity()).getDrawerLayout())
//                .build();
//        NavigationUI.setupActionBarWithNavController((MainActivity) getActivity(), navController,appBarConfiguration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.framelayout_nav_dishes, container, false);

        DishesTabLayoutAdapter dishesTabLayoutAdapter = new DishesTabLayoutAdapter(this);
        ViewPager2 viewPager = view.findViewById(R.id.dishes_tablelayout_pager);
        viewPager.setAdapter(dishesTabLayoutAdapter);

        TabLayout tabLayout = view.findViewById(R.id.dishes_tablayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.dishes_tablayout_dishes);
                    tab.setIcon(R.drawable.ic_local_dining_black_24dp);
                    break;
                case 1:
                    tab.setText(R.string.dishes_tablayout_mainfood);
                    tab.setIcon(R.drawable.ic_local_pizza_black_24dp);
                    break;
                case 2:
                    tab.setText(R.string.dishes_tablayout_drink);
                    tab.setIcon(R.drawable.ic_local_cafe_black_24dp);
                    break;
                case 3:
                    tab.setText(R.string.dishes_tablayout_dessert);
                    tab.setIcon(R.drawable.ic_cake_black_24dp);
                    break;
            }
        }
        ).attach();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu_dishes_commit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_placeOrder) {
            Bundle bundle = new Bundle();
            bundle.putString("amount", "textAmount");
            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_dishes_to_fragment_commit, bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        DishesTabLayoutAdapter dishesTabLayoutAdapter = new DishesTabLayoutAdapter(this);
//        ViewPager2 viewPager = view.findViewById(R.id.dishes_tablelayout_pager);
//        viewPager.setAdapter(dishesTabLayoutAdapter);
//
//        TabLayout tabLayout = view.findViewById(R.id.dishes_tablayout);
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            switch (position) {
//                case 0:
//                    tab.setText(R.string.dishes_tablayout_dishes);
//                    tab.setIcon(R.drawable.ic_local_dining_black_24dp);
//                    break;
//                case 1:
//                    tab.setText(R.string.dishes_tablayout_mainfood);
//                    tab.setIcon(R.drawable.ic_local_pizza_black_24dp);
//                    break;
//                case 2:
//                    tab.setText(R.string.dishes_tablayout_drink);
//                    tab.setIcon(R.drawable.ic_local_cafe_black_24dp);
//                    break;
//                case 3:
//                    tab.setText(R.string.dishes_tablayout_dessert);
//                    tab.setIcon(R.drawable.ic_cake_black_24dp);
//                    break;
//            }
//        }
//        ).attach();
//    }
}
