package com.shawn.smartrestaurant.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.ui.login.LoginActivity;

import java.util.List;
import java.util.Objects;

public class FragmentProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.button_delete).setOnClickListener(v -> {

            MainActivity mainActivity = (MainActivity) getActivity();

            Objects.requireNonNull(mainActivity).getLocalDb().userDao().delete(mainActivity.getUser());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}
