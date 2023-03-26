package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 姿态估计
 */
public class PoseEstimatorTest {

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            PoseEstimator poseEstimator = new PoseEstimator(new SeetaModelSetting(FileConstant.pose_estimation, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
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
