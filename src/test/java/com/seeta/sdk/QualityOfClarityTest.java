package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 非深度学习的人脸清晰度评估器
 *
 */
public class QualityOfClarityTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\10.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //AgePredictor agePredictor = new AgePredictor(new SeetaModelSetting(-1, age_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(2, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            QualityOfClarity qualityOfClarity = new QualityOfClarity();

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                float[] ages = new float[1];

                QualityOfClarity.QualityLevel check = qualityOfClarity.check(image, seetaRect, pointFS, ages);
                System.out.println(check);
                System.out.println(Arrays.toString(ages));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
