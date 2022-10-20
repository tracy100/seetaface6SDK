package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 攻击人脸检测
 */
public class AntiSpoofingTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    /**
     * 初始化加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //三个模型文件
        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        // 这里传两个模型才能准确得出结果 （fas_first和fas_second）
        String[] fas_first = {CSTA_PATH + "/fas_first.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_GPU));
            //关键点定位器face_landmarker_pts5 就是五个关键点，face_landmarker_pts68就是68个关键点，根据模型文件来的
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_GPU));
            //攻击人脸检测器
            FaceAntiSpoofing faceAntiSpoofing = new FaceAntiSpoofing(new SeetaModelSetting(0, fas_first, SeetaDevice.SEETA_DEVICE_GPU));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                int[] ints = new int[5];
                faceLandmarker.mark(image, seetaRect, pointFS,ints);
                FaceAntiSpoofing.Status predict = faceAntiSpoofing.Predict(image, seetaRect, pointFS);
                System.out.println(predict);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
