package com.shawn.smartrestaurant.ui.main.dishes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shawn.smartrestaurant.R;

public class DishesCategoryFragment extends Fragment {

    static final String PARAM_CATEGORY = "category";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.framelayout_tab_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Bundle args = getArguments();

        // 找到目标控件
        RecyclerView recyclerView = view.findViewById(R.id.category_recyclerView);

        // 垂直线性布局
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        // 初始化适配器并绑定
        DishesRecyclerViewAdapter dishesRecyclerViewAdapter = new DishesRecyclerViewAdapter(args.getStringArray(PARAM_CATEGORY));
        recyclerView.setAdapter(dishesRecyclerViewAdapter);
    }
}
