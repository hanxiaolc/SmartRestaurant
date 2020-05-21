package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.gson.Gson;
import com.shawn.smartrestaurant.db.entity.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DishesTabLayoutAdapter extends FragmentStateAdapter {

    //
    private Map<String, List<Dish>> dishMap;


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
        List<String> keyList = new ArrayList<>(dishMap.keySet());
        Collections.sort(keyList);

        return DishesCategoryFragment.newInstance(new Gson().toJson(this.dishMap.get(keyList.get(position))));
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return dishMap.size();
    }

    /**
     *
     */
    public Map<String, List<Dish>> getDishMap() {
        return dishMap;
    }

    /**
     *
     */
    public void setDishMap(Map<String, List<Dish>> dishMap) {
        this.dishMap = dishMap;
    }
}
