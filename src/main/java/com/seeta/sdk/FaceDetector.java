package com.seeta.sdk;


/**
 * 人脸检测器 检测到的每个人脸位置，用矩形表示。
 *
 * @author YaoCai Lin
 */
public class FaceDetector {
//    static {
//        System.loadLibrary("SeetaFaceDetector600_java");
//    }

    public long impl = 0;

    public FaceDetector(SeetaModelSetting setting) throws Exception {
        this.construct(setting);
    }

    private native void construct(SeetaModelSetting setting) throws Exception;

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native SeetaRect[] Detect(SeetaImageData image);

    public native void set(Property property, double value);

    public native double get(Property property);

    public enum Property {
        PROPERTY_MIN_FACE_SIZE(0),
        PROPERTY_THRESHOLD(1),
        PROPERTY_MAX_IMAGE_WIDTH(2),
        PROPERTY_MAX_IMAGE_HEIGHT(3),
        PROPERTY_NUMBER_THREADS(4),
        PROPERTY_ARM_CPU_MODE(0x101);

        private int value;

        private Property(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
 
    /*
    public native void SetComputingThreads(int num);
    public native int GetComputingThreads();


    public native void SetMinFaceSize(int size);
    public native int GetMinFaceSize();

    public native void SetThresh(float thresh);
    public native float GetThresh();

    public native void SetMaxImageWidth(int width);
    public native int GetMaxImageWidth();

    public native void SetMaxImageHeight(int height);
    public native int GetMaxImageHeight();
    */
}
