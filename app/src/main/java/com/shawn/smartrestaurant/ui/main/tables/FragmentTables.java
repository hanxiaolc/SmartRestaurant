package com.shawn.smartrestaurant.ui.main.tables;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 *
 */
public class FragmentTables extends Fragment {

    //
    private MainActivity activity;

    //
    private static final String ARG_PARAM1 = "param1";

    //
    private static final String ARG_PARAM2 = "param2";

    //
    private String mParam1;

    //
    private String mParam2;


    /**
     *
     */
    public FragmentTables() {
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

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

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

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentTables = inflater.inflate(R.layout.framelayout_nav_tables, container, false);
        RecyclerView recyclerView = fragmentTables.findViewById(R.id.tables_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        TablesRecyclerViewAdapter tablesRecyclerViewAdapter = new TablesRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(tablesRecyclerViewAdapter);

        return fragmentTables;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.tables_recyclerView);
        TablesRecyclerViewAdapter adapter = (TablesRecyclerViewAdapter) recyclerView.getAdapter();

        if (null == ((MainActivity) requireActivity()).getTableMap()) {
            ((MainActivity) requireActivity()).setTableMap(new HashMap<>());

            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                int i = 0;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table table = ds.toObject(Table.class);
                    ((MainActivity) requireActivity()).getTableMap().put(Objects.requireNonNull(table).getId(), table);

                    Objects.requireNonNull(adapter).getTableList().add(table);
                    adapter.notifyItemInserted(i);
                    i++;
                }
            });
        } else {
            if (null == Objects.requireNonNull(adapter).getTableList() || 0 == adapter.getTableList().size()) {
                int i = 0;
                for (Map.Entry entry : ((MainActivity) requireActivity()).getTableMap().entrySet()) {
                    Objects.requireNonNull(adapter).getTableList().add((Table) entry.getValue());
                    adapter.notifyItemInserted(i);
                    i++;
                }
            }
        }
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

            ((MainActivity) requireActivity()).setTableMap(new HashMap<>());

            // TODO Add onFailureListener
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                RecyclerView recyclerView = requireView().findViewById(R.id.tables_recyclerView);
                TablesRecyclerViewAdapter adapter = (TablesRecyclerViewAdapter) recyclerView.getAdapter();
                Objects.requireNonNull(adapter).getTableList().clear();
                adapter.notifyDataSetChanged();

                int i = 0;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table table = ds.toObject(Table.class);
                    ((MainActivity) requireActivity()).getTableMap().put(Objects.requireNonNull(table).getId(), table);

                    adapter.getTableList().add(ds.toObject(Table.class));
                    adapter.notifyItemInserted(i);
                    i++;
                }

                // Release blocking UI and hide progress bar
                requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                requireView().findViewById(R.id.progressBar_tables).setVisibility(View.GONE);
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
