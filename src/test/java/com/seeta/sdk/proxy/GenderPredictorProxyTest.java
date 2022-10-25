package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.GenderPredictorProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 性别识别
 */
public class GenderPredictorProxyTest {

    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    //图片路径
    public static String fileName = "E:\\111.jpg";

    // 拼接模型路径
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    // 人脸关键点定位模型路径拼接
    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    //人脸性别判断
    public static String[] gender_predictor_cstas = {CSTA_PATH + "/gender_predictor.csta"};

    //加载dll
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

        //性别识别器对象池配置
        SeetaConfSetting genderPredictorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, gender_predictor_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //性别识别器对象池代理 ， spring boot可以用GenderPredictorProxy来配置Bean
        GenderPredictorProxy genderPredictorProxy = new GenderPredictorProxy(genderPredictorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //5点定位
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                GenderPredictorProxy.GenderItem genderItem = genderPredictorProxy.predictGenderWithCrop(image, pointFS);
                //输出结果
                System.out.println(genderItem.getGender());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
