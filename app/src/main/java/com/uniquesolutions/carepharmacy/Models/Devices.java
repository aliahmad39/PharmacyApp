package com.uniquesolutions.carepharmacy.Models;

public class Devices {
    String id , name , price , image , status ;

    public Devices() {
    }

    public Devices(String id, String name, String price, String image, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
