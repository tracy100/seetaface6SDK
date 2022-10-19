package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;


/**
 * 非深度学习的人脸姿态评估器
 */
public class QualityOfPoseTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(-1, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(-1, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));


            QualityOfPose qualityOfPose = new QualityOfPose();


            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[68];
                faceLandmarker.mark(image, seetaRect, pointFS);
                float[] ages = new float[1];
                QualityOfPose.QualityLevel check = qualityOfPose.check(image, seetaRect, pointFS, ages);
                System.out.println(Arrays.toString(ages));
                System.out.println(check);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
