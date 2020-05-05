package com.shawn.smartrestaurant.ui.main.tables;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTables#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTables extends Fragment {

    //
    private MainActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTables() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment framelayout_nav_tables.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTables newInstance(String param1, String param2) {
        FragmentTables fragment = new FragmentTables();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == activity) {
            activity = (MainActivity) getActivity();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // initial current fragment in activity
        List<Fragment> fragments = Objects.requireNonNull((MainActivity) getActivity()).getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments();
        if (null == ((MainActivity) getActivity()).getCurrentFragment() && 1 == fragments.size() && fragments.get(0) instanceof FragmentTables) {
            ((MainActivity) getActivity()).setCurrentFragment(fragments.get(0));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 找到目标控件
        View fragmentTables = inflater.inflate(R.layout.framelayout_nav_tables, container, false);
        RecyclerView recyclerView = fragmentTables.findViewById(R.id.tables_recyclerView);

        // 垂直线性布局
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        // 初始化适配器并绑定
        TablesRecyclerViewAdapter tablesRecyclerViewAdapter = new TablesRecyclerViewAdapter();
        recyclerView.setAdapter(tablesRecyclerViewAdapter);

        return fragmentTables;
    }
}
