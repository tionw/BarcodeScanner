package com.example.tion.finalproject;

import java.io.Serializable;

public class Location2 implements Serializable{
    public double lon;
    public double lat;
    public Boolean link;
    public String name;

    public Location2(){

    }
    public double getLat() {
        return lat;
    }

    public void setLat(double type) {
        this.lat = type;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double type) {
        this.lon = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }
}
