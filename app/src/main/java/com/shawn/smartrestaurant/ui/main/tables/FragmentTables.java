package com.shawn.smartrestaurant.ui.main.tables;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 *
 */
public class FragmentTables extends Fragment {

    /**
     *
     */
    public class DishesReadyListenerTextWatcher implements TextWatcher {

        //
        private TablesRecyclerViewAdapter adapter;

        /**
         *
         */
        DishesReadyListenerTextWatcher(TablesRecyclerViewAdapter adapter) {
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

            this.adapter.setTableList(new ArrayList<>(((MainActivity) requireActivity()).getTableMap().values()));
            this.adapter.notifyDataSetChanged();

            // Release blocking UI and hide progress bar
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_tables).setVisibility(View.GONE);

            s.clear();
        }
    }

    /**
     *
     */
    public FragmentTables() {
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment framelayout_nav_tables.
//     */
//    public static FragmentTables newInstance(String param1, String param2) {
//        FragmentTables fragment = new FragmentTables();
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

        ((MainActivity) requireActivity()).authenticate();

        setHasOptionsMenu(true);

        // initial current fragment in activity
//        List<Fragment> fragments = Objects.requireNonNull((MainActivity) getActivity()).getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments();
//        if (1 == fragments.size() && fragments.get(0) instanceof FragmentTables) {
//            ((MainActivity) getActivity()).setCurrentFragment(fragments.get(0));
//        }
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle(Code.ActionBarTitle.DEFAULT.value);

        View fragmentTables = inflater.inflate(R.layout.framelayout_nav_tables, container, false);
        RecyclerView recyclerView = fragmentTables.findViewById(R.id.tables_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        TablesRecyclerViewAdapter tablesRecyclerViewAdapter = new TablesRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(tablesRecyclerViewAdapter);

        if (null == ((MainActivity) requireActivity()).getTableMap()) {
            ((MainActivity) requireActivity()).setTableMap(new HashMap<>());
        }
        ((TextView) fragmentTables.findViewById(R.id.textView_tables_listener)).addTextChangedListener(new DishesReadyListenerTextWatcher((TablesRecyclerViewAdapter) recyclerView.getAdapter()));

        return fragmentTables;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Block UI and show progress bar
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requireView().findViewById(R.id.progressBar_tables).setVisibility(View.VISIBLE);

        ((MainActivity) requireActivity()).dataUpdate(null, view.findViewById(R.id.textView_tables_listener), null, null);
    }

    /**
     *
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.option_menu_refresh, menu);
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.button_menu_refresh) {

            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_tables).setVisibility(View.VISIBLE);

            ((MainActivity) requireActivity()).dataUpdate(null, requireView().findViewById(R.id.textView_tables_listener), null, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
