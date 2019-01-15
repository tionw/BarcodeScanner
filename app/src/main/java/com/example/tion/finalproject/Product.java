package com.example.tion.finalproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Product implements Serializable {
    @PrimaryKey(autoGenerate=true)
    public int id2;

    @ColumnInfo
    public String domain;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String brand;

    @ColumnInfo
    public String link;

    @ColumnInfo
    public String id;

    @ColumnInfo
    public String img;

    @ColumnInfo
    public float price;

    public Product() {
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


}