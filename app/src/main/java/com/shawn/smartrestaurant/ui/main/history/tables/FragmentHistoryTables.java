package com.shawn.smartrestaurant.ui.main.history.tables;

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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Objects;


/**
 *
 */
public class FragmentHistoryTables extends Fragment {


    /**
     *
     */
    public FragmentHistoryTables() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment framelayout_nav_history_tables.
     */
    public static FragmentHistoryTables newInstance(String param1, String param2) {
        FragmentHistoryTables fragment = new FragmentHistoryTables();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).authenticate();

        setHasOptionsMenu(true);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentHistoryTables = inflater.inflate(R.layout.framelayout_nav_history_tables, container, false);
        RecyclerView recyclerView = fragmentHistoryTables.findViewById(R.id.recyclerView_history_tables);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        HistoryTablesRecyclerViewAdapter historyTablesRecyclerViewAdapter = new HistoryTablesRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(historyTablesRecyclerViewAdapter);

        return fragmentHistoryTables;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_history_tables);
        HistoryTablesRecyclerViewAdapter adapter = (HistoryTablesRecyclerViewAdapter) recyclerView.getAdapter();

        if (null == ((MainActivity) requireActivity()).getHistoryTableList()) {
            ((MainActivity) requireActivity()).setHistoryTableList(new ArrayList<>());
            // TODO Add onFailureListener
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_HISTORY).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).orderBy(Table.COLUMN_END_TIME, Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get history in FragmentHistoryTables.onViewCreated.");

                int i = 0;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table historyTable = ds.toObject(Table.class);
                    Objects.requireNonNull(adapter).getHistoryTableList().add(historyTable);
                    adapter.notifyItemInserted(i);
                    i++;

                    ((MainActivity) requireActivity()).getHistoryTableList().add(historyTable);
                }
            });
        } else {
            int i = 0;
            for (Table historyTable : ((MainActivity) requireActivity()).getHistoryTableList()) {
                Objects.requireNonNull(adapter).getHistoryTableList().add(historyTable);
                adapter.notifyItemInserted(i);
                i++;
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
            RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView_history_tables);
            HistoryTablesRecyclerViewAdapter adapter = (HistoryTablesRecyclerViewAdapter) recyclerView.getAdapter();

            ((MainActivity) requireActivity()).setHistoryTableList(new ArrayList<>());
            // TODO Add onFailureListener
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_HISTORY).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).orderBy(Table.COLUMN_END_TIME, Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get history in FragmentHistoryTables when Refresh button is clicked.");

                int i = 0;
                Objects.requireNonNull(adapter).getHistoryTableList().clear();
                adapter.notifyDataSetChanged();

                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table historyTable = ds.toObject(Table.class);
                    Objects.requireNonNull(adapter).getHistoryTableList().add(historyTable);
                    adapter.notifyItemInserted(i);
                    i++;

                    ((MainActivity) requireActivity()).getHistoryTableList().add(historyTable);
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
