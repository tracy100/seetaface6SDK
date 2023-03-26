package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 非深度的人脸亮度评估器
 */
public class QualityOfBirghtnessTest {


    public static String TEST_PICT = "D:\\face\\image\\me\\88.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

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
