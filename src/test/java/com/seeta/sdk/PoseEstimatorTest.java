package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 姿态估计
 */
public class PoseEstimatorTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\88.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] pose_estimation = {CSTA_PATH + "/pose_estimation.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            PoseEstimator poseEstimator = new PoseEstimator(new SeetaModelSetting(0, pose_estimation, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[68];
                faceLandmarker.mark(image, seetaRect, pointFS);

                float[] yaw = new float[1];//偏航
                float[] pitch = new float[1]; //
                float[] roll = new float[1]; //翻滚
                poseEstimator.Estimate(image, seetaRect,yaw,pitch,roll);
                System.out.println(Arrays.toString(yaw));
                System.out.println(Arrays.toString(pitch));
                System.out.println(Arrays.toString(roll));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
