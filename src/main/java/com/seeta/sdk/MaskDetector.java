package com.seeta.sdk;


/**
 * 口罩检测器
 */
public class MaskDetector {
//    static {
//        System.loadLibrary("SeetaMaksDetector200_java");
//    }

    public long impl = 0;

    public MaskDetector(SeetaModelSetting setting) throws Exception {
        this.construct(setting);
    }

    private native void construct(SeetaModelSetting setting) throws Exception;

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    /**
     * 人脸口罩检测器
     *
     * @param imageData [input]
     * @param face      [input]
     * @param score     [output]
     * @return boolean
     */
    public native boolean detect(SeetaImageData imageData, SeetaRect face, float[] score);
}
