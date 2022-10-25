package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 年龄检测
 */
public class AgeTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //模型文件
        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] age_cstas = {CSTA_PATH + "/age_predictor.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            //人脸检测
            FaceDetector detector = new FaceDetector(
                    new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸框关键点定位
            FaceLandmarker faceLandmarker = new FaceLandmarker(
                    new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            //年龄检测器
            AgePredictor agePredictor = new AgePredictor(
                    new SeetaModelSetting(0, age_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //图片数据
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
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
