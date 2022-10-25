package com.seeta.sdk.proxy;

import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 人脸特征相似度 1：1
 * 人脸相似度计算
 */
public class FaceRecognizerProxyTest {


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    // 拼接模型路径
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    // 人脸关键点定位模型路径拼接
    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    //高精度人脸特征提取模型，1024维
    public static String[] recognizer_cstas = {CSTA_PATH + "/face_recognizer.csta"};

    public static String fileName = "E:\\111.jpg";
    public static String fileName2 = "E:\\222.jpg";


    public static void main(String[] args) {

        //人脸识别检测器对象池配置，可以配置对象的个数哦
        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        //人脸关键点定位器对象池配置
        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        SeetaConfSetting faceRecognizerPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, recognizer_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸特征提取器，人脸特征相似度计算器
        FaceRecognizerProxy faceRecognizerProxy = new FaceRecognizerProxy(faceRecognizerPoolSetting);

        SeetaImageData image1 = SeetafaceUtil.toSeetaImageData(fileName);
        SeetaImageData image2 = SeetafaceUtil.toSeetaImageData(fileName2);

        try {
            //照片1
            SeetaRect[] detects1 = faceDetectorProxy.detect(image1);
            //detects1[0] 图片中可能有多个人脸（也可能没有人脸），这里演示只取第一个
            SeetaPointF[] pointFS1 = faceLandmarkerProxy.mark(image1, detects1[0]);
            //向量特征数组
            float[] features1 = faceRecognizerProxy.extract(image1, pointFS1);

            //照片2
            SeetaRect[] detects2 = faceDetectorProxy.detect(image2);
            //detects1[0] 图片中可能有多个人脸（也可能没有人脸），这里演示只取第一个
            SeetaPointF[] pointFS2 = faceLandmarkerProxy.mark(image2, detects2[0]);
            //向量特征数组
            float[] features2 = faceRecognizerProxy.extract(image2, pointFS2);

            //对比向量特征数组
            if (features1 != null && features2 != null) {
                float calculateSimilarity = faceRecognizerProxy.cosineSimilarity(features1, features2);
                System.out.printf("相似度:%f\n", calculateSimilarity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
