package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DishesTabLayoutAdapter extends FragmentStateAdapter {

    private String[] dataSet1 = {"a1", "a2", "a3", "a4", "a5", "a1", "a2", "a3", "a4", "a5", "a1", "a2", "a3", "a4", "a5"};
    private String[] dataSet2 = {"b1", "b2", "b3", "b4", "b5", "b1", "b2", "b3", "b4", "b5", "b1", "b2", "b3", "b4", "b5"};
    private String[] dataSet3 = {"c1", "c2", "c3", "c4", "c5", "c1", "c2", "c3", "c4", "c5", "c1", "c2", "c3", "c4", "c5"};
    private String[] dataSet4 = {"d1", "d2", "d3", "d4", "d5", "d1", "d2", "d3", "d4", "d5", "d1", "d2", "d3", "d4", "d5"};

    DishesTabLayoutAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new DishesCategoryFragment();
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putStringArray(DishesCategoryFragment.PARAM_CATEGORY, dataSet1);
                fragment.setArguments(args);
                break;
            case 1:
                args.putStringArray(DishesCategoryFragment.PARAM_CATEGORY, dataSet2);
                fragment.setArguments(args);
                break;
            case 2:
                args.putStringArray(DishesCategoryFragment.PARAM_CATEGORY, dataSet3);
                fragment.setArguments(args);
                break;
            case 3:
                args.putStringArray(DishesCategoryFragment.PARAM_CATEGORY, dataSet4);
                fragment.setArguments(args);
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
