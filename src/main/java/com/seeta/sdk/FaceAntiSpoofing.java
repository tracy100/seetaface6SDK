package com.seeta.sdk;


/**
 * 活体识别
 *
 * @author YaoCai Lin
 */
public class FaceAntiSpoofing {
//	static{
//		System.loadLibrary("FaceAntiSpoofing600_java");
//	}

    public long impl = 0;

    public FaceAntiSpoofing(SeetaModelSetting setting) {
        this.construct(setting);
    }

    public FaceAntiSpoofing(String model, String device, int id) {
        this.construct(model, device, id);
    }

    public FaceAntiSpoofing(String model1, String model2, String device, int id) {
        this.construct(model1, model2, device, id);
    }

    private native void construct(SeetaModelSetting setting);

    private native void construct(String model, String device, int id);

    private native void construct(String model1, String model2, String device, int id);

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    private native int PredictCore(SeetaImageData image, SeetaRect face, SeetaPointF[] landmarks);

    private native int PredictVideoCore(SeetaImageData image, SeetaRect face, SeetaPointF[] landmarks);

    public Status Predict(SeetaImageData image, SeetaRect face, SeetaPointF[] landmarks) {
        int status_num = this.PredictCore(image, face, landmarks);
        return Status.values()[status_num];
    }

    public Status PredictVideo(SeetaImageData image, SeetaRect face, SeetaPointF[] landmarks) {
        int status_num = this.PredictVideoCore(image, face, landmarks);
        return Status.values()[status_num];
    }

    public native void ResetVideo();

    public native void GetPreFrameScore(float[] clarity, float[] reality);

    public native void SetVideoFrameCount(int number);

    public native int GetVideoFrameCount();

    public native void SetThreshold(float clarity, float reality);

    public native void GetThreshold(float[] clarity, float[] reality);

    public enum Status {
        REAL,         //< 真实人脸
        SPOOF,        //< 攻击人脸（假人脸）
        FUZZY,        //< 无法判断（人脸成像质量不好）
        DETECTING,    //< 正在检测
    }

}