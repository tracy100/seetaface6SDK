package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class GenderPredictorTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] gender_cstas = {CSTA_PATH + "/gender_predictor.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(-1, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            GenderPredictor genderPredictor = new GenderPredictor(new SeetaModelSetting(-1, gender_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(-1, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);

            SeetaRect[] detects = detector.Detect(image);

            int i = 1;

            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[faceLandmarker.number()];

                faceLandmarker.mark(image, seetaRect, pointFS);

                GenderPredictor.GENDER[] gender = new GenderPredictor.GENDER[1];

                genderPredictor.PredictGenderWithCrop(image, pointFS, gender);

                System.out.printf("第%s张脸的性别为:%s\n", i++, gender[0].toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
