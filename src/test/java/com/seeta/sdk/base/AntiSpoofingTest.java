package com.seeta.sdk.base;


import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 攻击人脸检测
 */
public class AntiSpoofingTest {

    /**
     * 初始化加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //关键点定位器face_landmarker_pts5 就是五个关键点，face_landmarker_pts68就是68个关键点，根据模型文件来的
            FaceLandmarker faceLandmarker = new FaceLandmarker(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));
            //攻击人脸检测器
            FaceAntiSpoofing faceAntiSpoofing = new FaceAntiSpoofing(
                    new SeetaModelSetting(FileConstant.fas_arr, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                int[] ints = new int[5];
                faceLandmarker.mark(image, seetaRect, pointFS, ints);
                FaceAntiSpoofing.Status predict = faceAntiSpoofing.Predict(image, seetaRect, pointFS);
                System.out.println(predict);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
