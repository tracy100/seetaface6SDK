package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.GenderPredictorProxy;
import com.seeta.proxy.MaskDetectorProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

/**
 * 口罩检测器
 */
public class MaskDetectorProxyTest {

    //模型文件夹路径
    public static String CSTA_PATH = "E:\\models";

    //图片路径
    public static String fileName = "E:\\111.jpg";

    //模型文件代码
    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
    public static String[] mask_detector_cstas = {CSTA_PATH + "/mask_detector.csta"};


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

        //口罩检测器对象池配置，可以配置对象的个数哦
        SeetaConfSetting maskDetectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, mask_detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //口罩检测器对象池代理 ， spring boot可以用MaskDetectorProxy来配置Bean
        MaskDetectorProxy maskDetectorProxy = new MaskDetectorProxy(maskDetectorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                MaskDetectorProxy.MaskItem detect = maskDetectorProxy.detect(image, seetaRect);
                System.out.println(detect.getMask());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
