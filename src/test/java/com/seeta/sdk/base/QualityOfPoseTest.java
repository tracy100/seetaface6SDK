package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;


/**
 * 非深度学习的人脸姿态评估器
 */
public class QualityOfPoseTest {



    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));
            QualityOfPose qualityOfPose = new QualityOfPose();

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[5];
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
