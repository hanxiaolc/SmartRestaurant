package com.shawn.smartrestaurant.ui.main.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class FragmentMenu extends Fragment {


    /**
     *
     */
    public class ReadyListenerTextWatcher implements TextWatcher {

        //
        private MenuRecyclerViewAdapter adapter;

        /**
         *
         */
        ReadyListenerTextWatcher(MenuRecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        /**
         *
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

            if (!Code.READY.equals(s.toString())) {
                return;
            }

            this.adapter.setDishList(prepareDishList(((MainActivity) requireActivity()).getDishList()));
            this.adapter.notifyDataSetChanged();

            // Release blocking UI and hide progress bar
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_menu).setVisibility(View.GONE);

            s.clear();
        }
    }


    /**
     *
     */
    public FragmentMenu() {
    }


//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentMenus.
//     */
//    public static FragmentMenu newInstance(String param1, String param2) {
//        FragmentMenu fragment = new FragmentMenu();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        ((MainActivity) requireActivity()).authenticate();

        // Remember to set has option menu true
        setHasOptionsMenu(true);

        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the fragmentView and recyclerView
        View fragmentMenu = inflater.inflate(R.layout.framelayout_nav_menu, container, false);
        RecyclerView recyclerView = fragmentMenu.findViewById(R.id.recyclerView_menu);

        // Set LayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        // Set default dividing line
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // Bind RecyclerViewAdapter to recyclerView
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        ((TextView) fragmentMenu.findViewById(R.id.textView_menu_listener)).addTextChangedListener(new ReadyListenerTextWatcher(adapter));

        return fragmentMenu;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Block UI and show progress bar
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireView().findViewById(R.id.progressBar_menu).setVisibility(View.VISIBLE);

        ((MainActivity) requireActivity()).dataUpdate(view.findViewById(R.id.textView_menu_listener), null, null, null);
    }

    /**
     *
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.option_menu_add_menu, menu);

        if (!((MainActivity) requireActivity()).getUser().isManager()) {
            menu.getItem(0).setVisible(false);
        }
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.button_menu_add_menu) {
            NavHostFragment.findNavController(this).navigate(R.id.action_framelayout_nav_menu_to_framelayout_nav_addmenu, null);
            return true;
        }

        if (item.getItemId() == R.id.button_menu_refresh) {

            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_menu).setVisibility(View.VISIBLE);

            ((MainActivity) requireActivity()).dataUpdate(requireView().findViewById(R.id.textView_menu_listener), null, null, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private static List<Dish> prepareDishList(List<Dish> dishList) {
        if (null == dishList) {
            return new ArrayList<>();
        }

        List<Dish> result = new ArrayList<>();
        List<String> category = new ArrayList<>();

        for (Dish dish : dishList) {
            // Add group title
            if (!category.contains(dish.getCategory())) {
                category.add(dish.getCategory());

                Dish groupTitle = new Dish();
                groupTitle.setId(Code.INITIAL_DISH_ID);
                groupTitle.setCategory(dish.getCategory());
                result.add(groupTitle);
            }
            result.add(dish);
        }

        Collections.sort(result, (o1, o2) -> {
            int rltByCategory = o1.getCategory().compareTo(o2.getCategory());
            if (0 == rltByCategory) {
                return o1.getId().compareTo(o2.getId());
            }
            return rltByCategory;
        });

        return result;
    }

    // TODO delete
    public List<Dish> setUpDishList() {

        Dish dish1 = new Dish();
        dish1.setId("10000001");
        dish1.setGroup("0000");
        dish1.setDishName("Coca (1)");
        dish1.setPrice(0.25);
        dish1.setCategory(Code.DishCategory.DRINK.value);

        Dish dish2 = new Dish();
        dish2.setId("10000002");
        dish2.setGroup("0000");
        dish2.setDishName("Coca (2)");
        dish2.setPrice(0.25);
        dish2.setCategory(Code.DishCategory.MAIN_FOOD.value);

        Dish dish3 = new Dish();
        dish3.setId("10000003");
        dish3.setGroup("0000");
        dish3.setDishName("Coca (1)");
        dish3.setPrice(0.25);
        dish3.setCategory(Code.DishCategory.DRINK.value);

        Dish dish4 = new Dish();
        dish4.setId("10000004");
        dish4.setGroup("0000");
        dish4.setDishName("Coca (2)");
        dish4.setPrice(0.25);
        dish4.setCategory(Code.DishCategory.MAIN_FOOD.value);

        Dish dish5 = new Dish();
        dish5.setId("10000005");
        dish5.setGroup("0000");
        dish5.setDishName("Coca (3)");
        dish5.setPrice(0.25);
        dish5.setCategory(Code.DishCategory.DESSERT.value);

        Dish dish6 = new Dish();
        dish6.setId("10000006");
        dish6.setGroup("0000");
        dish6.setDishName("Coca (4)");
        dish6.setPrice(0.25);
        dish6.setCategory(Code.DishCategory.FLAVOR.value);

        Dish dish7 = new Dish();
        dish7.setId("10000007");
        dish7.setGroup("0000");
        dish7.setDishName("Coca (3)");
        dish7.setPrice(0.25);
        dish7.setCategory(Code.DishCategory.DESSERT.value);

        Dish dish8 = new Dish();
        dish8.setId("10000008");
        dish8.setGroup("0000");
        dish8.setDishName("Coca (4)");
        dish8.setPrice(0.25);
        dish8.setCategory(Code.DishCategory.FLAVOR.value);

        Dish dish9 = new Dish();
        dish9.setId("10000009");
        dish9.setGroup("0000");
        dish9.setDishName("Coca (5)");
        dish9.setPrice(0.25);
        dish9.setCategory(Code.DishCategory.DISH.value);

        Dish dish10 = new Dish();
        dish10.setId("10000010");
        dish10.setGroup("0000");
        dish10.setDishName("Coca (5)");
        dish10.setPrice(0.25);
        dish10.setCategory(Code.DishCategory.DISH.value);

        Dish dish11 = new Dish();
        dish11.setId(Code.INITIAL_DISH_ID);
        dish11.setCategory(Code.DishCategory.DISH.value);

        Dish dish12 = new Dish();
        dish12.setId(Code.INITIAL_DISH_ID);
        dish12.setCategory(Code.DishCategory.DRINK.value);

        Dish dish13 = new Dish();
        dish13.setId(Code.INITIAL_DISH_ID);
        dish13.setCategory(Code.DishCategory.MAIN_FOOD.value);

        Dish dish14 = new Dish();
        dish14.setId(Code.INITIAL_DISH_ID);
        dish14.setCategory(Code.DishCategory.FLAVOR.value);

        Dish dish15 = new Dish();
        dish15.setId(Code.INITIAL_DISH_ID);
        dish15.setCategory(Code.DishCategory.DESSERT.value);
        List<Dish> dishList = Arrays.asList(dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9, dish10, dish11, dish12, dish13, dish14, dish15);
        Collections.sort(dishList, (o1, o2) -> {
            int result = o1.getCategory().compareTo(o2.getCategory());
            if (0 == result) {
                return o1.getId().compareTo(o2.getId());
            }
            return result;
        });
        return dishList;
    }
}
