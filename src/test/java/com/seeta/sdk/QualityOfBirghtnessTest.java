package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 非深度的人脸亮度评估器
 */
public class QualityOfBirghtnessTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\88.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(-1, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(-1, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            QualityOfBrightness qualityOfBrightness = new QualityOfBrightness();

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);

            SeetaRect[] detects = detector.Detect(image);

            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[5];

                faceLandmarker.mark(image, seetaRect, pointFS);

                float[] floats = new float[1];

                QualityOfBrightness.QualityLevel check = qualityOfBrightness.check(image, seetaRect, pointFS, floats);

                System.out.println(Arrays.toString(floats));
                System.out.println(check);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
