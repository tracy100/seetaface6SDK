package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.EyeStateDetectorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 眼睛状态检测
 */
public class EyeStateDetectorProxyTest {

    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    //图片路径
    public static String TEST_PICT = "E:\\111.jpg";


    // 拼接模型路径
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    // 人脸关键点定位模型路径拼接
    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
    //眼睛状态评估模型
    public static String[] eye_cstas = {CSTA_PATH + "/eye_state.csta"};

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
                new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        //眼睛状态检测器 对象池配置
        SeetaConfSetting eyeStateDetectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, eye_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //眼睛状态检测器对象池代理 ， spring boot可以用EyeStateDetectorProxy来配置Bean
        EyeStateDetectorProxy eyeStateDetectorProxy = new EyeStateDetectorProxy(eyeStateDetectorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                EyeStateDetector.EYE_STATE[] detect = eyeStateDetectorProxy.detect(image, pointFS);
                System.out.println(Arrays.toString(detect));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
