package com.seeta.sdk;

/**
 * 眼睛状态估计器
 * @author YaoCai Lin
 */
public class EyeStateDetector {
//    static{
//        System.loadLibrary("SeetaEyeStateDetector200_java");
//    }

    public long impl = 0;

    public EyeStateDetector(SeetaModelSetting setting) throws Exception {
        this.construct(setting);
    }

    private native void construct(SeetaModelSetting setting) throws Exception;

    public native void dispose();

    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

//    public void Detect(SeetaImageData imageData, SeetaPointF[] points, EYE_STATE[] eyeStatus) {
//        if (eyeStatus.length < 2) return;
//
//        int[] eyeStateIndexs = new int[2];
//        DetectCore(imageData, points, eyeStateIndexs);
//        eyeStatus[0] = EYE_STATE.values()[eyeStateIndexs[0]];
//        eyeStatus[1] = EYE_STATE.values()[eyeStateIndexs[1]];
//    }

    /**
     * 眼睛状态
     * @param imageData
     * @param points
     * @return  EYE_STATE[]
     */
    public EYE_STATE[] detect(SeetaImageData imageData, SeetaPointF[] points) {
        EYE_STATE[] eyeStatus = new EYE_STATE[2];
        int[] eyeStateIndexs = new int[2];
        DetectCore(imageData, points, eyeStateIndexs);
        eyeStatus[0] = EYE_STATE.values()[eyeStateIndexs[0]];
        eyeStatus[1] = EYE_STATE.values()[eyeStateIndexs[1]];

        return eyeStatus;
    }

    private native void DetectCore(SeetaImageData imageData, SeetaPointF[] points, int[] eyeStateIndexs);

    public enum EYE_STATE {
        EYE_CLOSE,
        EYE_OPEN,
        EYE_RANDOM,
        EYE_UNKNOWN
    }
}
