package com.seeta.sdk.base;


import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 年龄检测
 */
public class AgeTest {

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测
            FaceDetector detector = new FaceDetector(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸框关键点定位
            FaceLandmarker faceLandmarker = new FaceLandmarker(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            //年龄检测器
            AgePredictor agePredictor = new AgePredictor(
                    new SeetaModelSetting(FileConstant.age_predictor, SeetaDevice.SEETA_DEVICE_AUTO));
            //图片数据
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                //输出年龄
                int[] ages = new int[1];
                agePredictor.PredictAgeWithCrop(image, pointFS, ages);
                System.out.println(Arrays.toString(ages));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
