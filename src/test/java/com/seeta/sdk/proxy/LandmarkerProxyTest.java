package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 使用对象池
 * 人脸遮挡评估测试
 */
public class LandmarkerProxyTest {

    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    //图片路径
    public static String fileName = "E:\\111.jpg";

    // 拼接模型路径
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
    public static String[] face_landmarker_mask_pts5_cstas = {CSTA_PATH + "/face_landmarker_mask_pts5.csta"};


    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //人脸识别检测器对象池配置，可以配置对象的个数哦
        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        //人脸关键点定位器对象池配置，并可以检测遮挡
        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, face_landmarker_mask_pts5_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        try {
            //图片
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            //检测人脸，识别到人脸
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //人脸关键点定位，人脸遮挡的点位
                LandmarkerMask mask = faceLandmarkerProxy.isMask(image, seetaRect);
                //输出
                System.out.println(Arrays.toString(mask.getSeetaPointFS()));
                System.out.println(Arrays.toString(mask.getMasks()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
