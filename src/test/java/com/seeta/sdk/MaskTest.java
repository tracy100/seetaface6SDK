package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class MaskTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] mask_cstas = {CSTA_PATH + "/mask_detector.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            MaskDetector maskDetector = new MaskDetector(new SeetaModelSetting(0, mask_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));


            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                float[] floats = new float[1];
                boolean flag = maskDetector.detect(image, seetaRect, floats);
                System.out.println(flag + ":   " + floats[0]);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
