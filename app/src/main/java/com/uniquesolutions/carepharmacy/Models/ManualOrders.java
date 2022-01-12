package com.uniquesolutions.carepharmacy.Models;

public class ManualOrders {
    String userName,email,userId,PhnNo,prescImage , address;
    String medId , medctgr , medName , medPrice , medDsc , medQty , medType , medImage , medStatus;
    long ordertime;

    public ManualOrders() {
    }

    public ManualOrders(String userName, String email, String userId, String phnNo, String prescImage, String address, String medId, String medctgr, String medName, String medPrice, String medDsc, String medQty, String medType, String medImage, String medStatus, long ordertime) {
        this.userName = userName;
        this.email = email;
        this.userId = userId;
        PhnNo = phnNo;
        this.prescImage = prescImage;
        this.address = address;
        this.medId = medId;
        this.medctgr = medctgr;
        this.medName = medName;
        this.medPrice = medPrice;
        this.medDsc = medDsc;
        this.medQty = medQty;
        this.medType = medType;
        this.medImage = medImage;
        this.medStatus = medStatus;
        this.ordertime = ordertime;
    }

    public ManualOrders(String userName, String email, String phnNo, String address, String medId, String medctgr, String medName, String medPrice, String medDsc, String medQty, String medImage, String medStatus, long ordertime) {
        this.userName = userName;
        this.email = email;
        PhnNo = phnNo;
        this.address = address;
        this.medId = medId;
        this.medctgr = medctgr;
        this.medName = medName;
        this.medPrice = medPrice;
        this.medDsc = medDsc;
        this.medQty = medQty;
        this.medImage = medImage;
        this.medStatus = medStatus;
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

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getMedctgr() {
        return medctgr;
    }

    public void setMedctgr(String medctgr) {
        this.medctgr = medctgr;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedPrice() {
        return medPrice;
    }

    public void setMedPrice(String medPrice) {
        this.medPrice = medPrice;
    }

    public String getMedDsc() {
        return medDsc;
    }

    public void setMedDsc(String medDsc) {
        this.medDsc = medDsc;
    }

    public String getMedQty() {
        return medQty;
    }

    public void setMedQty(String medQty) {
        this.medQty = medQty;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMedImage() {
        return medImage;
    }

    public void setMedImage(String medImage) {
        this.medImage = medImage;
    }

    public String getMedStatus() {
        return medStatus;
    }

    public void setMedStatus(String medStatus) {
        this.medStatus = medStatus;
    }

    public long getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(long ordertime) {
        this.ordertime = ordertime;
    }
}
