package com.seeta.sdk.thread;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_CPU);

            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_CPU));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_CPU));

            faceRecognizerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_recognizer, SeetaDevice.SEETA_DEVICE_CPU));

    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static  FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);
    
    //人脸特征提取器，人脸特征相似度计算器
    public static  FaceRecognizerProxy faceRecognizerProxy = new FaceRecognizerProxy(faceRecognizerPoolSetting);


    public static void main(String[] args) {
        
        int threadCount = 5;

        System.out.println("使用线程数: " + threadCount);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 提交任务到线程池
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new MyThread());
        }

        // 关闭线程池
        executor.shutdown();

    }

    /**
     * 获取特征数组
     *
     * @author YaoCai Lin
     * @time 2020年7月15日 下午12:10:56
     */
    private static void extract() {
        long startTime = System.nanoTime(); // 记录开始时间

        try {

            String fileName = "E:\\face\\image\\me\\00.jpg";
            String fileName2 = "E:\\face\\image\\me\\11.jpg";
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
                    long endTime = System.nanoTime(); // 记录结束时间
                    double durationMs = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
                    System.out.printf(Thread.currentThread().getName() + ",相似度:%f,耗时:%.2fms\n", calculateSimilarity, durationMs);
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
