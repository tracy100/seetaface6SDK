package com.seeta.sdk.proxy;


import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 县城池测试
 */
public class FaceRecognizerProxyTest {


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    public static String[] recognizer_cstas = {CSTA_PATH + "/face_recognizer_mask.csta"};


    public static String fileName = "D:\\face\\image\\me\\00.jpg";
    public static String fileName2 = "D:\\face\\image\\me\\mask2.jpg";


    public static void main(String[] args) {

        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        SeetaConfSetting faceRecognizerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, recognizer_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceRecognizerProxy faceRecognizerProxy = new FaceRecognizerProxy(faceRecognizerPoolSetting);

        for (int j = 0; j < 4; j++) {
            new Thread(() -> {
                long start = System.currentTimeMillis();
                for (int i = 0; i < 100; i++) {
                    SeetaImageData image1 = SeetafaceUtil.toSeetaImageData(fileName);
                    SeetaImageData image2 = SeetafaceUtil.toSeetaImageData(fileName2);

                    float[] features1 = null;
                    float[] features2 = null;
                    try {

                        SeetaRect[] detects1 = faceDetectorProxy.detect(image1);
                        for (SeetaRect seetaRect : detects1) {
                            SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image1, seetaRect);
                            features1 = faceRecognizerProxy.extract(image1, pointFS);
                        }

                        SeetaRect[] detects2 = faceDetectorProxy.detect(image2);
                        for (SeetaRect seetaRect : detects2) {
                            SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image2, seetaRect);
                            features2 = faceRecognizerProxy.extract(image2, pointFS);
                        }

                        if (features1 != null && features2 != null) {
                            float calculateSimilarity = faceRecognizerProxy.cosineSimilarity(features1, features2);
                            System.out.printf("第" + i + "相似度:%f\n", calculateSimilarity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println((end - start) / 1000);
            }).start();
        }


    }


}
