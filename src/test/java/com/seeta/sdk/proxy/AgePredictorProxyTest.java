package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.AgePredictorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;


/**
 * 年龄检测器
 */
public class AgePredictorProxyTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;


    //人脸年龄检测器对象池配置
    public static SeetaConfSetting agePredictorPoolSetting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_GPU);
        try {
            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            agePredictorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(FileConstant.age_predictor, SeetaDevice.SEETA_DEVICE_AUTO));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

    //人脸年龄检测器对象池代理 ， spring boot可以用AgePredictorProxy来配置Bean
    public static AgePredictorProxy agePredictorProxy = new AgePredictorProxy(agePredictorPoolSetting);


    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) throws FileNotFoundException {

        try {
            //图片
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            //检测人脸，识别到人脸
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //人脸关键点定位
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                //识别年龄
                int age = agePredictorProxy.predictAgeWithCrop(image, pointFS);
                System.out.println(age);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
