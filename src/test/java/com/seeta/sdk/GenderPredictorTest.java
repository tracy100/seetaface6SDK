package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 性别识别器
 */
public class GenderPredictorTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] gender_cstas = {CSTA_PATH + "/gender_predictor.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //性别识别器
            GenderPredictor genderPredictor = new GenderPredictor(new SeetaModelSetting(0, gender_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //照片
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            //识别到的人脸
            SeetaRect[] detects = detector.Detect(image);

            int i = 1;

            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[faceLandmarker.number()];

                faceLandmarker.mark(image, seetaRect, pointFS);

                GenderPredictor.GENDER[] gender = new GenderPredictor.GENDER[1];
                //检测性别
                genderPredictor.PredictGenderWithCrop(image, pointFS, gender);

                System.out.printf("第%s张脸的性别为:%s\n", i++, gender[0].toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
