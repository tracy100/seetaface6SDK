package com.seeta.sdk;

import java.io.FileNotFoundException;

/**
 * 人脸跟踪器,人脸跟踪器会对输入的彩色图像或者灰度图像中的人脸进行跟踪，并返回所有跟踪到的人脸信息。
 */
public class FaceTracker {
//    static{
//        System.loadLibrary("FaceTracker600_java");
//    }

    public long impl = 0;

    public FaceTracker(String seetaModel, int videoWidth, int videoHeight) {
        this.construct(seetaModel, videoWidth, videoHeight);
    }

    public FaceTracker(String[] seetaModel, int videoWidth, int videoHeight) throws FileNotFoundException {
        if (seetaModel == null || seetaModel.length < 1 ) {
            throw new FileNotFoundException("模型文件没找到");
        }
        this.construct(seetaModel[0], videoWidth, videoHeight);
    }

    /**
     * 后面 自己加的 可能会用到gpu
     *
     * @param model
     * @param device
     * @param id
     * @param videoWidth
     * @param videoHeight
     */
    public FaceTracker(String model, String device, int id, int videoWidth, int videoHeight) {
        this.construct(model, device, id, videoWidth, videoHeight);
    }
    //( jstring model, jstring device, jint id, jint video_width, jint video_height)

    /**
     * 后面 自己加的 可能会用到gpu
     *
     * @param model
     * @param device
     * @param id
     * @param videoWidth
     * @param videoHeight
     */
    private native void construct(String model, String device, int id, int videoWidth, int videoHeight);

    private native void construct(String seetaModel, int videoWidth, int videoHeight);

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native void SetSingleCalculationThreads(int num);

    public native SeetaTrackingFaceInfo[] Track(SeetaImageData image);

    public native SeetaTrackingFaceInfo[] Track(SeetaImageData image, int frame_no);

    public native void SetMinFaceSize(int size);

    public native int GetMinFaceSize();

    public native void SetVideoStable(boolean stable);

    public native boolean GetVideoStable();
}
