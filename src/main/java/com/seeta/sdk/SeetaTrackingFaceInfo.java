package com.seeta.sdk;

public class SeetaTrackingFaceInfo {
    public int x;
    public int y;
    public int width;
    public int height;
    public float score;
    public int frame_no;
    public int PID;


    @Override
    public String toString() {
        return "SeetaTrackingFaceInfo{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", score=" + score +
                ", frame_no=" + frame_no +
                ", PID=" + PID +
                '}';
    }
}
