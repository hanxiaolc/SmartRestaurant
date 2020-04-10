package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shawn.smartrestaurant.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDishes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDishes extends Fragment {

    RecyclerView dishesRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDishes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment framelayout_nav_dishes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDishes newInstance(String param1, String param2) {
        FragmentDishes fragment = new FragmentDishes();
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

        return view;
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
