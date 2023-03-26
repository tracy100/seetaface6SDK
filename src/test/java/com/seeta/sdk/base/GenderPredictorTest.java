package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 性别识别器
 */
public class GenderPredictorTest {

    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));
            //性别识别器
            GenderPredictor genderPredictor = new GenderPredictor(new SeetaModelSetting(FileConstant.gender_predictor, SeetaDevice.SEETA_DEVICE_AUTO));
            //照片
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
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
