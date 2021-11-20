package com.huawei.finalassignment.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    private String Fullname;
    private String Email;
    private String AvatarString;

    public Account(String fullname, String email, String avatarString) {
        Fullname = fullname;
        Email = email;
        AvatarString = avatarString;
    }

    protected Account(Parcel in) {
        Fullname = in.readString();
        Email = in.readString();
        AvatarString = in.readString();
    }

    
    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAvatarString() {
        return AvatarString;
    }

    public void setAvatarString(String avatarString) {
        AvatarString = avatarString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Fullname);
        parcel.writeString(Email);
        parcel.writeString(AvatarString);
    }
}
