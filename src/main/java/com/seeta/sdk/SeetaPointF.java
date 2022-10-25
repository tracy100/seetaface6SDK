package com.seeta.sdk;

public class SeetaPointF {
    public double x;
    public double y;

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

    @Override
    public String toString() {
        return "SeetaPointF{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
