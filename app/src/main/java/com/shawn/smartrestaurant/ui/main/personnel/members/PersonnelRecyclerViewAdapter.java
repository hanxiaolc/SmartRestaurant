package com.shawn.smartrestaurant.ui.main.personnel.members;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.ui.main.personnel.add.FragmentPersonnelAdd;

import java.util.List;

/**
 *
 */
public class PersonnelRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<User> memberList;


    /**
     *
     */
    PersonnelRecyclerViewAdapter(List<User> memberList) {
        this.memberList = memberList;
    }


    /**
     *
     */
    public static class PersonnelRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        View view;

        PersonnelRecyclerViewAdapterViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }


    /**
     *
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View personnelItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_personnel, parent, false);
        return new PersonnelRecyclerViewAdapterViewHolder(personnelItemView);
    }

    /**
     *
     */

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User member = this.memberList.get(position);

        TextView userId = holder.itemView.findViewById(R.id.textView_personnel_user_id);
        TextView manager = holder.itemView.findViewById(R.id.textView_personnel_manager);

        userId.setText(member.getId());
        if (member.isManager()) {
            manager.setText("MANAGER");
        }

        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentPersonnelAdd.ARG_MEMBER, new Gson().toJson(member));
            Navigation.findNavController(view)
                    .navigate(R.id.action_framelayout_nav_personnel_members_to_framelayout_nav_personnel_add, bundle);
        });
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    /**
     *
     */
    public List<User> getMemberList() {
        return memberList;
    }

    /**
     *
     */
    public void setMemberList(List<User> memberList) {
        this.memberList = memberList;
    }
}
