package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.AgePredictorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;


/**
 * 使用对象池
 * 年龄检测器
 */
public class AgePredictorProxyTest {


    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    public static String[] age_predictor_cstas = {CSTA_PATH + "/age_predictor.csta"};

    public static String fileName = "D:\\face\\image\\me\\00.jpg";

    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //人脸识别检测器对象池配置，可以配置对象的个数哦
        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        //人脸关键点定位器对象池配置
        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        //人脸年龄检测器对象池配置
        SeetaConfSetting agePredictorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, age_predictor_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸年龄检测器对象池代理 ， spring boot可以用AgePredictorProxy来配置Bean
        AgePredictorProxy agePredictorProxy = new AgePredictorProxy(agePredictorPoolSetting);

        try {
            //图片
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
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
