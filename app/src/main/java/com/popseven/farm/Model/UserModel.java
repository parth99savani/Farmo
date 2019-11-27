package com.popseven.farm.Model;

import java.util.ArrayList;
import java.util.Collection;

public class UserModel {

    private String mobileNo;
    private String name;
    private String emailId;
    private ArrayList<String> favoriteFarms;

    public UserModel(){

    }

    public UserModel(String mobileNo, String name, String emailId, ArrayList<String> favoriteFarms) {
        this.mobileNo = mobileNo;
        this.name = name;
        this.emailId = emailId;
        this.favoriteFarms = favoriteFarms;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public ArrayList<String> getFavoriteFarms() {
        return favoriteFarms;
    }

    public void setFavoriteFarms(ArrayList<String> favoriteFarms) {
        this.favoriteFarms = favoriteFarms;
    }
}
