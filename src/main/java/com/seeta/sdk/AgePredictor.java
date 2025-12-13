package com.seeta.sdk;

/**
 * 年龄估计器
 *
 * @author YaoCai Lin
 *
 */
public class AgePredictor {
//    static{
//        System.loadLibrary("SeetaAgePredictor600_java");
//    }

    public long impl = 0;

    public AgePredictor(SeetaModelSetting setting) {
        this.construct(setting);
    }

    private native void construct(SeetaModelSetting setting);

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native int GetCropFaceWidth();

    public native int GetCropFaceHeight();

    public native int GetCropFaceChannels();

    public native boolean CropFace(SeetaImageData image, SeetaPointF[] points, SeetaImageData face);

    public native boolean PredictAge(SeetaImageData face, int[] age);

    public native boolean PredictAgeWithCrop(SeetaImageData image, SeetaPointF[] points, int[] age);

    /**
     * 获取照片的年龄评估
     *
     * @param image  SeetaImageData
     * @param points points
     * @return 将接口重写，使其符合java代码正常写法
     */
    public Integer predictAgeWithCrop(SeetaImageData image, SeetaPointF[] points) {
        int[] ages = new int[1];
        this.PredictAgeWithCrop(image, points, ages);

        if (ages != null && ages.length >= 1) {
            return ages[0];
        }
        return null;
    }

    public native void set(Property property, double value);

    public native double get(Property property);

    public enum Property {
        PROPERTY_NUMBER_THREADS(4),
        PROPERTY_ARM_CPU_MODE(5);

        private int value;

        private Property(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
