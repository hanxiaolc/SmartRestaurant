package com.shawn.smartrestaurant.ui.main.dishes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.shawn.smartrestaurant.db.entity.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class DishesTabLayoutAdapter extends FragmentStateAdapter {

    //
    private Map<String, List<Dish>> dishCategoryMap;


    /**
     *
     */
    DishesTabLayoutAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    /**
     *
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        List<String> keyList = new ArrayList<>(dishCategoryMap.keySet());
        Collections.sort(keyList);
        Collections.sort(Objects.requireNonNull(this.dishCategoryMap.get(keyList.get(position))), (o1, o2) -> {

            if (null == o1.getDishCode()) {
                return 1;
            }
            if (null == o2.getDishCode()) {
                return -1;
            }

            return o1.getDishCode().compareTo(o2.getDishCode());
        });

        return new DishesCategoryFragment(this.dishCategoryMap.get(keyList.get(position)));
        //return DishesCategoryFragment.newInstance(new Gson().toJson(this.dishMap.get(keyList.get(position))));
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return dishCategoryMap.size();
    }

    /**
     *
     */
    public Map<String, List<Dish>> getDishCategoryMap() {
        return dishCategoryMap;
    }

    /**
     *
     */
    public void setDishCategoryMap(Map<String, List<Dish>> dishCategoryMap) {
        this.dishCategoryMap = dishCategoryMap;
    }
}
