package com.seeta.sdk.thread;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;


/**
 * 线程池测试
 */
public class PoolProxyThreadTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static  SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;


    public static  SeetaConfSetting faceRecognizerPoolSetting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_GPU);
        try {
            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_GPU));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_GPU));

            faceRecognizerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_GPU));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static  FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);
    
    //人脸特征提取器，人脸特征相似度计算器
    public static  FaceRecognizerProxy faceRecognizerProxy = new FaceRecognizerProxy(faceRecognizerPoolSetting);


    public static void main(String[] args) {
//创建多线程对象
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();
        MyThread mt3 = new MyThread();

        //调用start()方法，其内部调用了run()方法，实现了多线程
        //在这里直接调用run()方法，不能实现多线程
        mt1.start();
        mt2.start();
        mt3.start();

    }

    /**
     * 获取特征数组
     *
     * @author YaoCai Lin
     * @time 2020年7月15日 下午12:10:56
     */
    private static void extract() {
 
        try {

            String fileName = "D:\\face\\image\\me\\00.jpg";
            String fileName2 = "D:\\face\\image\\me\\11.jpg";
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
                    System.out.printf(Thread.currentThread().getName() + ",相似度:%f\n", calculateSimilarity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static class MyThread extends Thread {
        public void run() {
            for (int x = 0; x < 1000; x++) {
                extract();
            }
        }
    }

}
