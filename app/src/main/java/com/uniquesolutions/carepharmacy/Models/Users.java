package com.uniquesolutions.carepharmacy.Models;

public class Users {
    String userName,email,password,userId,PhnNo,prescImage , address;
   long ordertime;

    public Users(){}

    public Users(String userName, String email, String phnNo, String address, long ordertime) {
        this.userName = userName;
        this.email = email;
        PhnNo = phnNo;
        this.address = address;
        this.ordertime = ordertime;
    }

    public Users(String userName, String email, String password, String userId, String phnNo, String prescImage, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        PhnNo = phnNo;
        this.prescImage = prescImage;
        this.address = address;
    }

    public Users(String userName, String email, String password, String phnNo, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        PhnNo = phnNo;
        this.address = address;
    }

    public Users(String userName, String email, String userId, String phnNo, String prescImage, String address) {
        this.userName = userName;
        this.email = email;
        this.userId = userId;
        PhnNo = phnNo;
        this.prescImage = prescImage;
        this.address = address;
    }

    public Users(String userName, String email, String userId) {
        this.userName = userName;
        this.email = email;
        this.userId = userId;
    }

    public long getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(long ordertime) {
        this.ordertime = ordertime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhnNo() {
        return PhnNo;
    }

    public void setPhnNo(String phnNo) {
        PhnNo = phnNo;
    }

    public String getPrescImage() {
        return prescImage;
    }

    public void setPrescImage(String prescImage) {
        this.prescImage = prescImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
