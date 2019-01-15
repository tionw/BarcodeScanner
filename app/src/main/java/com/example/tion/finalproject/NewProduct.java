package com.example.tion.finalproject;

import java.io.Serializable;
import java.util.ArrayList;

public class NewProduct implements Serializable {
    public String domain;

    public String name;

    public String brand;

    public String link;

    public String id;

    public String img;

    public float price;

    public ArrayList<Product> offers;

    public NewProduct() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Product> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Product> offers) {
        this.offers = offers;
    }
}