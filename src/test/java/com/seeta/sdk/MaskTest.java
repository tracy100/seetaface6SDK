package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

//口罩检测
public class MaskTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    //加载dll
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //模型文件
        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        //口罩模型文件
        String[] mask_cstas = {CSTA_PATH + "/mask_detector.csta"};

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //口罩检测器
            MaskDetector maskDetector = new MaskDetector(new SeetaModelSetting(0, mask_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //图片数据
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[5];
                faceLandmarker.mark(image, seetaRect, pointFS);
                float[] floats = new float[1];
                //检测人脸是否戴口罩
                boolean flag = maskDetector.detect(image, seetaRect, floats);
                System.out.println(flag + ":   " + floats[0]);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
