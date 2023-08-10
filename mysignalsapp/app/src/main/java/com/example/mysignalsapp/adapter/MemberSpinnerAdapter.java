package com.example.mysignalsapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.entity.Member;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class MemberSpinnerAdapter extends ArrayAdapter<Member> {

    private ArrayList<Member> members;

    public MemberSpinnerAdapter(Context context, ArrayList<Member> members) {
        super(context, 0, members);
        this.members = members;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        // Set the member's name as the text for the dropdown item
        Member member = members.get(position);
        if (member != null) {
            ImageView imageView = convertView.findViewById(R.id.image);
            TextView textView = convertView.findViewById(R.id.name);
            imageView.setContentDescription(member.getPicture()); // Adjust to your Member model getter
            textView.setText(member.getSurname() + " " + member.getName()); // Adjust to your Member model getter
        }
        return convertView;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
        notifyDataSetChanged();
    }
}
