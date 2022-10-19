package com.seeta.sdk;

/**
 * 非深度学习的人脸完整度评估器，评估人脸靠近图像边缘的程度。
 */
public class QualityOfIntegrity {
//    static {
//        System.loadLibrary("QualityAssessor300_java");
//    }

    public long impl = 0;

    public QualityOfIntegrity() {
        this.construct();
    }

    public QualityOfIntegrity(float low, float high) {
        this.construct(low, high);
    }

    private native void construct();

    /**
     * @param low  [input] threshold low
     * @param high [input] threshold high
     */
    private native void construct(float low, float high);

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    /**
     * @param imageData [input]image data
     * @param face      [input] face location
     * @param landmarks [input] face landmarks
     * @param score     [output] quality score
     * @return QualityLevel quality level sorted into "LOW" , "Medium" , "HIGH"
     */
    private native int checkCore(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score);

    public QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score) {
        int index = this.checkCore(imageData, face, landmarks, score);

        QualityLevel level = QualityLevel.values()[index];
        return level;
    }

    public enum QualityLevel {
        LOW,//Quality level is low
        MEDIUM,//Quality level is medium
        HIGH,//Quality level is high
    }
}
