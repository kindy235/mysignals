package com.example.mysignalsapp.viewmodel;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.entity.Member;

public class UserDataViewModel extends BaseObservable {


    private Member member;
    public void setMember(Member member) {
        this.member = member;
        notifyChange();
    }
    public Member getMember() {
        return member;
    }

    @Bindable
    public String getPicture() {
        return member != null ? member.getPicture() : "";
    }

    @Bindable
    public String getName() {
        return member != null ? member.getName() : "";
    }

    @Bindable
    public String getSurname() {
        return member != null ? member.getSurname() : "";
    }
}
