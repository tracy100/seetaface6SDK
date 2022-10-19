package com.seeta.sdk;


/**
 * 非深度学习的人脸尺寸评估器。
 */
public class QualityOfResolution {

    public long impl = 0;

    /**
     * 人脸尺寸评估器构造函数。
     */
    public QualityOfResolution() {
        this.construct();
    }

    /**
     * 人脸尺寸评估器构造函数。
     *
     * @param low  分级参数一
     * @param high 分级参数二
     */
    public QualityOfResolution(float low, float high) {
        this.construct(low, high);
    }

    private native void construct();

    /**
     * 人脸尺寸评估器构造函数。
     *
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
     * 评估人脸尺寸
     *
     * @param imageData
     * @param face
     * @param landmarks
     * @param score     [output] quality score
     * @return int
     */
    private native int checkCore(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score);

    /**
     * 评估人脸尺寸
     *
     * @param imageData
     * @param face
     * @param landmarks
     * @param score     [output] quality score
     * @return QualityLevel
     */
    public QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score) {
        int index = this.checkCore(imageData, face, landmarks, score);

        QualityLevel level = QualityLevel.values()[index];
        return level;
    }

    /**
     * 人脸尺寸评估结果枚举
     */
    public enum QualityLevel {
        LOW,//Quality level is low
        MEDIUM,//Quality level is medium
        HIGH,//Quality level is high
    }
}
