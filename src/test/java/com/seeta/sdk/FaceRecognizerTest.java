package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;


/**
 * 人脸向量特征测试
 */
public class FaceRecognizerTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

        String[] recognizer_cstas = {CSTA_PATH + "/face_recognizer.csta"};

        int size = 68;

        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            FaceRecognizer faceRecognizer = new FaceRecognizer(new SeetaModelSetting(0, recognizer_cstas, SeetaDevice.SEETA_DEVICE_AUTO));


            System.out.println(faceRecognizer.GetExtractFeatureSize());

            String fileName = "D:\\face\\11.jpg";
            String fileName2 = "D:\\face\\22.jpg";

            SeetaImageData image1 = SeetafaceUtil.toSeetaImageData(fileName);

            SeetaRect[] detects1 = detector.Detect(image1);

            float[] features1 = new float[faceRecognizer.GetExtractFeatureSize()];

            //1
            SeetaPointF[] pointFS1 = new SeetaPointF[size];
            int[] masks1 = new int[size];
            faceLandmarker.mark(image1, detects1[0], pointFS1, masks1);
            faceRecognizer.Extract(image1, pointFS1, features1);
            SeetaImageData image2 = SeetafaceUtil.toSeetaImageData(fileName2);
            SeetaRect[] detects2 = detector.Detect(image2);

            //2
            float[] features2 = new float[faceRecognizer.GetExtractFeatureSize()];
            SeetaPointF[] pointFS2 = new SeetaPointF[size];
            int[] masks2 = new int[size];
            faceLandmarker.mark(image2, detects2[0], pointFS2, masks2);
            faceRecognizer.Extract(image2, pointFS2, features2);

            if (features1 != null && features2 != null) {
                float calculateSimilarity = faceRecognizer.CalculateSimilarity(features1, features2);
                System.out.printf("相似度:%f\n",  calculateSimilarity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
