package com.example.hannybuns.memorygame6.object;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by HannyBuns on 6/2/2018.
 */

public class Player implements Serializable {
    private int id;
    private String name;
    private String birthday;
    private int points;
    private LatLng myLoc;

    public Player(String name, String birthday, int points) {
        this.setName(name);
        this.setBirthday(birthday);
        this.setPoints(points);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Player: " + name+ ", Points: "+points;
    }

    public LatLng getMyLoc() {
        return myLoc;
    }

    public void setMyLoc(LatLng myLoc) {
        this.myLoc = myLoc;
    }
}
