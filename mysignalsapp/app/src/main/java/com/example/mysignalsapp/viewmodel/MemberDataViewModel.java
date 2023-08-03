package com.example.mysignalsapp.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.entity.Member;

public class MemberDataViewModel extends BaseObservable {

    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Bindable()
    public String getName(){
        return member.getName();
    }
}
