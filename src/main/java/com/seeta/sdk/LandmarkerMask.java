package com.seeta.sdk;

/**
 * 判断人脸遮挡
 */
public class LandmarkerMask {

    SeetaPointF[] seetaPointFS;
    int[] masks;

    public SeetaPointF[] getSeetaPointFS() {
        return seetaPointFS;
    }

    public void setSeetaPointFS(SeetaPointF[] seetaPointFS) {
        this.seetaPointFS = seetaPointFS;
    }

    public int[] getMasks() {
        return masks;
    }

    public void setMasks(int[] masks) {
        this.masks = masks;
    }
}
