package com.seeta.sdk.base;


import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 眼睛开闭状态检测
 */
public class EyeTest {


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {


        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //关键点定位器 5点
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            //眼睛状态检测器
            EyeStateDetector eyeStateDetector = new EyeStateDetector(new SeetaModelSetting(FileConstant.eye_state, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
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
