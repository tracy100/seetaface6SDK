package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;


/**
 * 人脸向量特征测试
 */
public class FaceRecognizerTest {

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //关键点定位器 5点
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            //人脸向量特征提取和对比器
            FaceRecognizer faceRecognizer = new FaceRecognizer(new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_AUTO));

            System.out.println(faceRecognizer.GetExtractFeatureSize());

            //两张图片
            String fileName = "D:\\face\\image\\me\\00.jpg";
            String fileName2 = "D:\\face\\image\\me\\11.jpg";
            //第1张照片
            SeetaImageData image1 = SeetafaceUtil.toSeetaImageData(fileName);
            //第一张照片人脸识别
            SeetaRect[] detects1 = detector.Detect(image1);

            SeetaPointF[] pointFS1 = new SeetaPointF[faceRecognizer.GetExtractFeatureSize()];
            int[] masks1 = new int[faceRecognizer.GetExtractFeatureSize()];
            //第一张图片，第一个人脸关键点定位，有多个人脸的情况下，只取第一个人脸（这是测试，先这样写）
            faceLandmarker.mark(image1, detects1[0], pointFS1, masks1);
            //第一张图片，第一个人脸向量特征提取features1
            float[] features1 = new float[faceRecognizer.GetExtractFeatureSize()];
            faceRecognizer.Extract(image1, pointFS1, features1);

            //第2张照片
            SeetaImageData image2 = SeetafaceUtil.toSeetaImageData(fileName2);

            //第二张图片，人脸识别
            SeetaRect[] detects2 = detector.Detect(image2);

            SeetaPointF[] pointFS2 = new SeetaPointF[faceRecognizer.GetExtractFeatureSize()];
            int[] masks2 = new int[faceRecognizer.GetExtractFeatureSize()];
            //第二张图片，第一个人脸，关键点识别
            faceLandmarker.mark(image2, detects2[0], pointFS2, masks2);

            //第二张图片，第一个人脸，向量特征提取features2
            float[] features2 = new float[faceRecognizer.GetExtractFeatureSize()];
            faceRecognizer.Extract(image2, pointFS2, features2);

            System.out.println(Arrays.toString(features1));
            System.out.println(Arrays.toString(features2));
            //两个人脸向量做对比，得出分数
            if (features1 != null && features2 != null) {
                float calculateSimilarity = faceRecognizer.CalculateSimilarity(features1, features2);
                System.out.printf("相似度:%f\n", calculateSimilarity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
