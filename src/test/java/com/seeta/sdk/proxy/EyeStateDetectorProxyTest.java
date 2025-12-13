package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.EyeStateDetectorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * 眼睛状态检测
 */
public class EyeStateDetectorProxyTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;


    //眼睛状态检测器 对象池配置
    public static SeetaConfSetting eyeStateDetectorPoolSetting;


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_CPU);
        detectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

        faceLandmarkerPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

        eyeStateDetectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(FileConstant.eye_state, SeetaDevice.SEETA_DEVICE_AUTO));
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

    //眼睛状态检测器对象池代理 ， spring boot可以用EyeStateDetectorProxy来配置Bean
    public static EyeStateDetectorProxy eyeStateDetectorProxy = new EyeStateDetectorProxy(eyeStateDetectorPoolSetting);


    public static void main(String[] args) throws FileNotFoundException {

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
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
