package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 眼睛开闭状态检测
 */
public class EyeTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\mask\\mask3.jpeg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //模型文件
        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        //眼睛状态检测的模型文件
        String[] eye_cstas = {CSTA_PATH + "/eye_state.csta"};
        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(-1, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(-1, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            //眼睛状态检测器
            EyeStateDetector eyeStateDetector = new EyeStateDetector(new SeetaModelSetting(-1, eye_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                EyeStateDetector.EYE_STATE[] eyeStatus = eyeStateDetector.detect(image, pointFS);
                System.out.println(Arrays.toString(eyeStatus));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
