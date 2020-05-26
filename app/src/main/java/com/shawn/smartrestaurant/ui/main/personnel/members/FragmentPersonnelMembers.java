package com.shawn.smartrestaurant.ui.main.personnel.members;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 *
 */
public class FragmentPersonnelMembers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     *
     */
    public FragmentPersonnelMembers() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPersonnelMembers.
     */
    public static FragmentPersonnelMembers newInstance(String param1, String param2) {
        FragmentPersonnelMembers fragment = new FragmentPersonnelMembers();
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

        setHasOptionsMenu(true);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentPersonnelMembers = inflater.inflate(R.layout.framelayout_nav_personnel_members, container, false);

        RecyclerView recyclerView = fragmentPersonnelMembers.findViewById(R.id.recyclerView_personnel_members);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        PersonnelRecyclerViewAdapter personnelRecyclerViewAdapter = new PersonnelRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(personnelRecyclerViewAdapter);
        return fragmentPersonnelMembers;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView_personnel_members);
        PersonnelRecyclerViewAdapter adapter = (PersonnelRecyclerViewAdapter) recyclerView.getAdapter();

        if (((MainActivity) requireActivity()).isPersonnelChanged() || null == ((MainActivity) requireActivity()).getMemberList() || ((MainActivity) requireActivity()).getMemberList().isEmpty()) {
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_USERS).whereEqualTo(User.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {

                Objects.requireNonNull(adapter).getMemberList().clear();
                ((MainActivity) requireActivity()).setMemberList(new ArrayList<>());
                adapter.notifyDataSetChanged();

                int i = 0;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    User member = ds.toObject(User.class);
                    Objects.requireNonNull(adapter).getMemberList().add(member);
                    adapter.notifyItemInserted(i);
                    i++;

                    ((MainActivity) requireActivity()).getMemberList().add(member);
                }
            });
        } else {
            Objects.requireNonNull(adapter).getMemberList().clear();
            adapter.notifyDataSetChanged();

            int i = 0;
            for (User member : ((MainActivity) requireActivity()).getMemberList()) {
                Objects.requireNonNull(adapter).getMemberList().add(member);
                adapter.notifyItemInserted(i);
                i++;

                ((MainActivity) requireActivity()).getMemberList().add(member);
            }
        }
    }

    /**
     *
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.option_menu_personnel, menu);

        if (!((MainActivity) requireActivity()).getUser().isManager()) {
            menu.getItem(R.id.button_menu_personnel_add_new).setVisible(false);
        }
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.button_menu_personnel_refresh) {

            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_personnel_members).setVisibility(View.VISIBLE);

            ((MainActivity) requireActivity()).setTableMap(new HashMap<>());

            // TODO Add onFailureListener
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_USERS).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView_personnel_members);
                PersonnelRecyclerViewAdapter adapter = (PersonnelRecyclerViewAdapter) recyclerView.getAdapter();
                Objects.requireNonNull(adapter).getMemberList().clear();
                adapter.notifyDataSetChanged();

                int i = 0;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    adapter.getMemberList().add(ds.toObject(User.class));
                    adapter.notifyItemInserted(i);
                    i++;
                }

                // Release blocking UI and hide progress bar
                requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                requireView().findViewById(R.id.progressBar_personnel_members).setVisibility(View.GONE);
            });

            return true;
        }

        if (item.getItemId() == R.id.button_menu_personnel_add_new) {
            NavHostFragment.findNavController(this).navigate(R.id.action_framelayout_nav_personnel_members_to_framelayout_nav_personnel_add, new Bundle());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
