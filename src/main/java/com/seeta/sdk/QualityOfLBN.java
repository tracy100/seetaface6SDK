package com.seeta.sdk;

/**
 * 深度学习的人脸清晰度评估器
 * 这个识别器不怎么好
 */
public class QualityOfLBN {
//    static {
//        System.loadLibrary("QualityAssessor300_java");
//    }

    public long impl = 0;

    public QualityOfLBN(SeetaModelSetting setting) throws Exception {
        this.construct(setting);
    }

    private native void construct(SeetaModelSetting setting) throws Exception;

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    /**
     * @param imageData [input] image data
     * @param points    [input] face points
     * @param light     [output] 0 means light is good, 1 is not [not recommend]      0表示光线好，1表示不好[不推荐]
     * @param blur      [output] o means face is clear, 1 means face is blur          o 表示脸部清晰，1 表示脸部模糊
     * @param noise     [output] 0 means having noise ,1 not [not recommend]          0 表示有噪音，1 表示没有 [不推荐]
     */
    public native void Detect(SeetaImageData imageData, SeetaPointF[] points, int[] light, int[] blur, int[] noise);

    public native void set(Property property, double value);

    public native double get(Property property);

    public enum LIGHTSTATE {
        BRIGHT,
        DARK
    }

    public enum BLURSTATE {
        CLEAR,
        BLUR
    }

    public enum NOISESTATE {
        HAVENOISE,
        NONOISE
    }

    public enum Property {
        PROPERTY_NUMBER_THREADS(4),
        PROPERTY_ARM_CPU_MODE(5),
        PROPERTY_LIGHT_THRESH(10),
        PROPERTY_BLUR_THRESH(11),
        PROPERTY_NOISE_THRESH(12);

        private int value;

        private Property(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
