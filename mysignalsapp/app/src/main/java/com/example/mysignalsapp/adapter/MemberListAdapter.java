package com.example.mysignalsapp.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.DeviceItemBinding;
import com.example.mysignalsapp.databinding.MemberItemBinding;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.utils.AddMemberDialog;
import com.example.mysignalsapp.utils.Util;
import com.example.mysignalsapp.viewmodel.HomeViewModel;
import com.example.mysignalsapp.viewmodel.UserDataViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    private ArrayList<Member> members;

    public interface MemberClickListener {
        void onMemberClick(Member member);
    }

    public interface MemberEditButtonListener {
        void onEditClick(Member member);
    }

    public interface MemberRemoveButtonListener {
        void onRemoveClick(Member member);
    }

    private MemberClickListener listener;
    private MemberEditButtonListener editButtonListener;
    private MemberRemoveButtonListener removeButtonListener;

    public MemberListAdapter(ArrayList<Member> members) {
        assert members != null;
        this.members = members;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MemberItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.member_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        Member member = members.get(position);
        if (member != null) {
            holder.userDataViewModel.setMember(member);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return members.size();
        } catch (Exception ex){return 0;}
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final UserDataViewModel userDataViewModel = new UserDataViewModel();
        ViewHolder(MemberItemBinding memberItemBinding) {
            super(memberItemBinding.getRoot());

            memberItemBinding.setUserDataViewModel(userDataViewModel);
            memberItemBinding.getRoot().setOnClickListener(view -> {

                Member member = userDataViewModel.getMember();
                if (member!=null && listener != null) {
                    listener.onMemberClick(member);
                }
            });
            ImageView editButton = memberItemBinding.getRoot().findViewById(R.id.edit_member_btn);
            editButton.setOnClickListener(v -> {
                // Show the AddMemberDialog when the FloatingActionButton is clicked
                Member member = userDataViewModel.getMember();
                if (member!=null) {
                    editButtonListener.onEditClick(member);
                }
            });

            ImageView removeButton = memberItemBinding.getRoot().findViewById(R.id.remove_member_btn);
            removeButton.setOnClickListener(v -> {
                // Show the AddMemberDialog when the FloatingActionButton is clicked
                Member member = userDataViewModel.getMember();
                if (member!=null) {
                    removeButtonListener.onRemoveClick(member);
                }
            });
        }
    }

    public void setMemberClickListener(MemberClickListener listener) {
        this.listener = listener;
    }

    public void setOnMemberEditButtonListener(MemberEditButtonListener listener) {
        this.editButtonListener = listener;
    }

    public void setOnMemberRemoveButtonListener(MemberRemoveButtonListener listener) {
        this.removeButtonListener = listener;
    }

    public void updateData(ArrayList<Member> newMembers) {
        if (newMembers!=null) {
            members = newMembers;
            notifyDataSetChanged();
        }
    }
}
