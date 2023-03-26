package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;


/**
 * 非深度学习的人脸完整度评估器，评估人脸靠近图像边缘的程度。
 */
public class QualityOfIntegrityTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\100.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {


        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);

            QualityOfIntegrity qualityOfIntegrity = new QualityOfIntegrity();

            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                float[] ages = new float[1];
                QualityOfIntegrity.QualityLevel check = qualityOfIntegrity.check(image, seetaRect, pointFS, ages);
                System.out.println(Arrays.toString(ages));
                System.out.println(check);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
