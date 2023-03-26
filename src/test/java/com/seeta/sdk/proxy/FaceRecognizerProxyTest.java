package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;

/**
 * 人脸特征相似度 1：1
 * 人脸相似度计算
 */
public class FaceRecognizerProxyTest {
    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;


    public static SeetaConfSetting faceRecognizerPoolSetting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        try {
            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            faceRecognizerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_AUTO));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

    //人脸特征提取器，人脸特征相似度计算器
    public static FaceRecognizerProxy faceRecognizerProxy = new FaceRecognizerProxy(faceRecognizerPoolSetting);

    public static String fileName = "D:\\face\\image\\me\\00.jpg";
    public static String fileName2 = "D:\\face\\image\\me\\11.jpg";

    public static void main(String[] args) throws FileNotFoundException {

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
