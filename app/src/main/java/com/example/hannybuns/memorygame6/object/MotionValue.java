package com.example.hannybuns.memorygame6.object;

import java.io.Serializable;

/**
 * Created by HannyBuns on 6/13/2018.
 */

public class MotionValue implements Serializable {
    private double x;
    private double y;
    private double z;
    private double latitude;

    public MotionValue(double x, double y, double z){
        setX(x);
        setY(y);
        setZ(z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return latitude;
    }
}
