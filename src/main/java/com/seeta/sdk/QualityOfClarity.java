package com.seeta.sdk;

/**
 * 非深度学习的人脸清晰度评估器
 */
public class QualityOfClarity {
//    static {
//        System.loadLibrary("QualityAssessor300_java");
//    }

    public long impl = 0;

    public QualityOfClarity() {
        this.construct();
    }

    /**
     * low	float		分级参数一
     * high	float		分级参数二
     * 说明：分类依据为[0, low)=> LOW; [low, high)=> MEDIUM; [high, ~)=> HIGH.
     *
     * @param low
     * @param high
     */
    public QualityOfClarity(float low, float high) {
        this.construct(low, high);
    }

    private native void construct();

    /**
     * @param low  [input] threshold low
     * @param high [input] threshold high
     *             [0, low)=> LOW
     *             [low, high)=> MEDIUM
     *             [high, ~)=> HIGH
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
