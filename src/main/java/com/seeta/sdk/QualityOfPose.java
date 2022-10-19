package com.seeta.sdk;

/**
 * 非深度学习的人脸姿态评估器
 */
public class QualityOfPose {
//    static {
//        System.loadLibrary("QualityAssessor300_java");
//    }

    public long impl = 0;

    public QualityOfPose() {
        this.construct();
    }

    private native void construct();

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

    /**
     * @param imageData [input]image data
     * @param face      [input] face location
     * @param landmarks [input] face landmarks
     * @param score     [output] quality score
     * @return QualityLevel quality level sorted into "LOW" , "Medium" , "HIGH"
     */
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
