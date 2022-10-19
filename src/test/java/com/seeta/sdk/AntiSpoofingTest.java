package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class AntiSpoofingTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] fas_first = {CSTA_PATH + "/fas_first.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_GPU));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_GPU));

            FaceAntiSpoofing faceAntiSpoofing = new FaceAntiSpoofing(new SeetaModelSetting(0, fas_first, SeetaDevice.SEETA_DEVICE_GPU));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                int[] ints = new int[5];
                faceLandmarker.mark(image, seetaRect, pointFS,ints);
                FaceAntiSpoofing.Status predict = faceAntiSpoofing.Predict(image, seetaRect, pointFS);
                System.out.println(predict);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
