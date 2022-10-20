package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.PoseEstimatorProxy;
import com.seeta.sdk.SeetaDevice;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaModelSetting;
import com.seeta.sdk.SeetaRect;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class PoseEstimatorProxyTest {


    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    public static String[] pose_estimation_cstas = {CSTA_PATH + "/pose_estimation.csta"};


    public static String fileName = "D:\\face\\image\\me\\00.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        //人脸识别检测器对象池配置，可以配置对象的个数哦
        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        SeetaConfSetting poseEstimatorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, pose_estimation_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        PoseEstimatorProxy poseEstimatorProxy = new PoseEstimatorProxy(poseEstimatorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {

                PoseEstimatorProxy.PoseItem estimate = poseEstimatorProxy.estimate(image, seetaRect);
                System.out.println(estimate.getYaw() + " -- " + estimate.getPitch() + " -- " + estimate.getRoll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
