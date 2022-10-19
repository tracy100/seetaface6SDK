package com.seeta.sdk;


/**
 * 姿态估计
 */
public class PoseEstimator {

    public long impl = 0;

    /**
     * 后面自己添加的
     *
     * @param seting
     */
    public PoseEstimator(SeetaModelSetting seting) {
        this.construct(seting);
    }

    public PoseEstimator(String seetaModel) {
        this.construct(seetaModel);
    }

    public PoseEstimator(String model, String device, int id) {
        this.construct(model, device, id);
    }

    /**
     * 后面自己添加的
     *
     * @param seting
     */
    private native void construct(SeetaModelSetting seting);

    private native void construct(String seetaModel);

    private native void construct(String model, String device, int id);

    public native void dispose();


    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native void Estimate(SeetaImageData image, SeetaRect face, float[] yaw, float[] pitch, float[] roll);
}
