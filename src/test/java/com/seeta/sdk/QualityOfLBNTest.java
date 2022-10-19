package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 深度学习的人脸清晰度评估器
 */
public class QualityOfLBNTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\33.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        //System.load("D:\\face\\seeta-sdk\\src\\main\\resources\\windows_x64\\QualityAssessor3JNI.dll");
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] qualityOfLBN_cstas = {CSTA_PATH + "/quality_lbn.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts68.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            QualityOfLBN qualityOfLBN = new QualityOfLBN(new SeetaModelSetting(0, qualityOfLBN_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {

                System.out.println(faceLandmarker.number());
                SeetaPointF[] pointFS = new SeetaPointF[faceLandmarker.number()];
                faceLandmarker.mark(image, seetaRect, pointFS);
               // public native void Detect(SeetaImageData imageData, SeetaPointF[] points, int[] light, int[] blur, int[] noise);
                int[] light = new int[1];

                int[] blur = new int[1];

                int[] noise = new int[1];

                qualityOfLBN.Detect(image,pointFS,light,blur,noise);

                System.out.println(Arrays.toString(light));
                System.out.println(Arrays.toString(blur));
                System.out.println(Arrays.toString(noise));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
