package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.AgePredictorProxy;
import com.seeta.proxy.FaceAntiSpoofingProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 活体检测，攻击人脸检测
 */
public class FaceAntiSpoofingProxyTest {


    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    //图片路径
    public static String fileName = "E:\\111.jpg";

    // 拼接模型路径
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    // 人脸关键点定位模型路径拼接
    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    //活体检测模型，两个
    public static  String[] fas = {CSTA_PATH + "/fas_first.csta",CSTA_PATH + "/fas_second.csta"};

    //加载dll
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

        SeetaConfSetting faceAntiSpoofingSetting = new SeetaConfSetting(new SeetaModelSetting(0, fas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceAntiSpoofingProxy faceAntiSpoofingProxy = new FaceAntiSpoofingProxy(faceAntiSpoofingSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                FaceAntiSpoofing.Status predict = faceAntiSpoofingProxy.predict(image,seetaRect, pointFS);
                //输出
                System.out.println(predict);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
