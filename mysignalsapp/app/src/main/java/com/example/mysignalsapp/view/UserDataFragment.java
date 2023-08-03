package com.example.mysignalsapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.MemberListAdapter;
import com.example.mysignalsapp.databinding.FragmentUserdataBinding;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.utils.AddMemberDialog;
import com.example.mysignalsapp.utils.ApiRequests;
import com.example.mysignalsapp.utils.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserDataFragment extends Fragment implements
        MemberListAdapter.MemberClickListener,
        MemberListAdapter.MemberEditButtonListener,
        MemberListAdapter.MemberRemoveButtonListener,
        AddMemberDialog.OnMemberAddedListener{

    private ArrayList<Member> memberList;
    private MemberListAdapter memberAdapter;
    private EditText editTextBirthday;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentUserdataBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_userdata, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and set its layout manager
        RecyclerView recyclerView = view.findViewById(R.id.member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberList = new ArrayList<>();
        // Create the adapter with the memberList and set it to the RecyclerView
        memberAdapter = new MemberListAdapter(memberList);
        memberAdapter.setMemberClickListener(this);
        memberAdapter.setOnMemberEditButtonListener(this);
        memberAdapter.setOnMemberRemoveButtonListener(this);
        recyclerView.setAdapter(memberAdapter);

        Button showMember = view.findViewById(R.id.btn_get_member);
        showMember.setOnClickListener(v -> {
            getAllMembers();
        });

        // Set up the FloatingActionButton
        FloatingActionButton fabAddMember = view.findViewById(R.id.add_member_btn);
        fabAddMember.setOnClickListener(v -> {
            // Show the AddMemberDialog when the FloatingActionButton is clicked
            AddMemberDialog dialog = AddMemberDialog.newInstance(null);
            dialog.setOnMemberAddedListener(this);
            dialog.show(getChildFragmentManager(), "AddMemberDialog");
        });
        getAllMembers();
    }

    public void getAllMembers() {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(getContext()).getAllMembers().enqueue(new Callback<ArrayList<Member>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<Member>> call, @NotNull Response<ArrayList<Member>> response) {
                if (response.isSuccessful()){
                    memberList = response.body();
                    memberAdapter.updateData(memberList);
                    //Toast.makeText(getContext(), "Data get successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<Member>> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMemberClick(Member member) {
        // Ouvrez le fragment DeviceFragmentInfo et transmettez les informations du dispositif
        MemberInfoFragment memberInfoFragment = new MemberInfoFragment();
        memberInfoFragment.setMember(member);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, memberInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onEditClick(Member member) {
        AddMemberDialog dialog = AddMemberDialog.newInstance(member);
        dialog.setOnMemberAddedListener(this);
        dialog.show(getChildFragmentManager(), "AddMemberDialog");
    }

    @Override
    public void onMemberAdded(Member member){
        ApiRequests.postMember(member, getContext());
        try {
            Thread.sleep(100);
        }catch (Exception ignored){
        }
        getAllMembers();
    }

    @Override
    public void onRemoveClick(Member member) {
        ApiRequests.deleteMember(member.getId(), getContext());
        try {
            Thread.sleep(100);
        }catch (Exception ignored){
        }
        getAllMembers();
    }
}