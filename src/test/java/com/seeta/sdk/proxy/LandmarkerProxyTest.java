package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.AgePredictorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;
import javafx.util.Pair;

import java.util.Arrays;


/**
 * 使用对象池
 * 年龄检测器
 */
public class LandmarkerProxyTest {


    public static String CSTA_PATH = "E:\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] face_landmarker_mask_pts5_cstas = {CSTA_PATH + "/face_landmarker_mask_pts5.csta"};


    public static String fileName = "E:\\111.jpg";

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

        //人脸关键点定位器对象池配置
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
                //人脸关键点定位
                Pair<SeetaPointF[], int[]> mask = faceLandmarkerProxy.isMask(image, seetaRect);
                System.out.println(Arrays.toString(mask.getKey()));
                System.out.println(Arrays.toString(mask.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
