package com.shawn.smartrestaurant.ui.main.personnel.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.Objects;


/**
 *
 */
public class FragmentPersonnelAdd extends Fragment {

    //
    public static final String ARG_MEMBER = "member";

    //
    private User member;

    /**
     *
     */
    public FragmentPersonnelAdd() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter user.
     * @return A new instance of fragment FragmentPersonnelAdd.
     */
    public static FragmentPersonnelAdd newInstance(String user) {
        FragmentPersonnelAdd fragment = new FragmentPersonnelAdd();
        Bundle args = new Bundle();
        args.putString(ARG_MEMBER, user);
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

        if (getArguments() != null) {
            this.member = new Gson().fromJson(getArguments().getString(ARG_MEMBER), User.class);
        }

        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppBarLayout) requireActivity().findViewById(R.id.appBarLayout_main)).setExpanded(true);

        return inflater.inflate(R.layout.framelayout_nav_personnel_add, container, false);
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextInputEditText userId = view.findViewById(R.id.editText_personnel_add_user_id);
        TextInputEditText password = view.findViewById(R.id.editText_personnel_add_password);
        TextInputEditText group = view.findViewById(R.id.editText_personnel_add_group);
        SwitchMaterial isManager = view.findViewById(R.id.switchMaterial_personnel_add_manager);
        Button delete = view.findViewById(R.id.button_personnel_add_delete);
        Button update = view.findViewById(R.id.button_personnel_add_update);
        Button addNew = view.findViewById(R.id.button_personnel_add_new_member);

        group.setFocusable(false);

        long currentTime = System.currentTimeMillis();
        if (null != this.member) {
            addNew.setVisibility(View.GONE);
            userId.setText(this.member.getId());
            password.setText(this.member.getPassword());
            group.setText(this.member.getGroup());
            isManager.setChecked(this.member.isManager());

            if (!((MainActivity) requireActivity()).getUser().isManager()) {
                userId.setFocusable(false);
                password.setFocusable(false);
                isManager.setClickable(false);
                delete.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
            } else {
                userId.setFocusable(false);

                // Owner user can not be edited by other users
                if (!((MainActivity) requireActivity()).getUser().getId().equals(this.member.getId()) && ((MainActivity) requireActivity()).getUser().getCreateUser().equals(this.member.getId())) {
                    password.setFocusable(false);
                    isManager.setClickable(false);
                    delete.setVisibility(View.GONE);
                    update.setVisibility(View.GONE);
                }
                // Owner can not delete himself and change his manager status
                if (((MainActivity) requireActivity()).getUser().getId().equals(this.member.getId())) {
                    isManager.setClickable(false);
                    delete.setVisibility(View.GONE);
                }

                delete.setOnClickListener(v -> {
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_USERS).document(this.member.getId()).delete().addOnSuccessListener(aVoid -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Delete user in FragmentPersonnelAdd where Delete button is clicked.");

                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_MEMBER_VERSION, currentTime);
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentPersonnelAdd when Delete button is clicked.");

//                        ((MainActivity) requireActivity()).setPersonnelChanged(true);
                        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        Navigation.findNavController(v)
                                .navigate(R.id.action_framelayout_nav_personnel_add_to_framelayout_nav_personnel_members, new Bundle());
                    });
                });
                // TODO Add onFailureListener
                update.setOnClickListener(v -> {
                    if (null == password.getText() || password.getText().toString().isEmpty()) {
                        new MaterialAlertDialogBuilder(requireContext()).setTitle("FAILURE").setMessage("A password is required.").setPositiveButton("OK", (dialog, which) -> {
                        }).show();
                        return;
                    }

                    this.member.setPassword(Objects.requireNonNull(password.getText()).toString().trim());
                    this.member.setManager(isManager.isChecked());
                    this.member.setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
                    this.member.setUpdateTime(currentTime);
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_USERS).document(this.member.getId()).set(member).addOnSuccessListener(aVoid -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set user in FragmentPersonnelAdd when Update button is clicked.");

                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_MEMBER_VERSION, currentTime);
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentPersonnelAdd when Update button is clicked.");

//                        ((MainActivity) requireActivity()).setPersonnelChanged(true);
                        new MaterialAlertDialogBuilder(requireContext()).setTitle("SUCCESS").setMessage("Member information is updated.").setPositiveButton("OK", (dialog, which) -> {

                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            Navigation.findNavController(v)
                                    .navigate(R.id.action_framelayout_nav_personnel_add_to_framelayout_nav_personnel_members, new Bundle());
                        }).show();
                    });
                });
            }
        } else {
            group.setText(((MainActivity) requireContext()).getUser().getGroup());
            delete.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            isManager.setChecked(false);

            addNew.setOnClickListener(v -> {
//                ((MainActivity) requireActivity()).setPersonnelChanged(true);

                User member = new User();
                member.setId(Objects.requireNonNull(userId.getText()).toString().trim());
                member.setPassword(Objects.requireNonNull(password.getText()).toString().trim());
                member.setGroup(((MainActivity) requireContext()).getUser().getGroup());
                member.setManager(isManager.isChecked());
                member.setCreateUser(((MainActivity) requireContext()).getUser().getCreateUser());
                member.setCreateTime(currentTime);
                member.setUpdateUser(((MainActivity) requireContext()).getUser().getId());
                member.setUpdateTime(currentTime);

                if (null == userId.getText() || userId.getText().toString().isEmpty() || null == password.getText() || password.getText().toString().isEmpty()) {
                    new MaterialAlertDialogBuilder(requireContext()).setTitle("FAILURE").setMessage("A user ID and password is required.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                // Validate User ID
                if (!member.validateUserId()) {
                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("You need to give a 4-16 alphabets or numbers for User ID.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_USERS).document(member.getId()).set(member).addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set user in FragmentPersonnelAdd when Add New button is clicked.");

                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_MEMBER_VERSION, currentTime);
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentPersonnelAdd when Add New button is clicked.");

//                    ((MainActivity) requireActivity()).setPersonnelChanged(true);
                    ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    Navigation.findNavController(v)
                            .navigate(R.id.action_framelayout_nav_personnel_add_to_framelayout_nav_personnel_members, new Bundle());
                });
            });
        }
    }
}
