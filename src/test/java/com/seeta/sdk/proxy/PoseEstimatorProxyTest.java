package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.PoseEstimatorProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;

public class PoseEstimatorProxyTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;

    public static SeetaConfSetting poseEstimatorPoolSetting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        try {
            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            poseEstimatorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(FileConstant.pose_estimation, SeetaDevice.SEETA_DEVICE_AUTO));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

    public static PoseEstimatorProxy poseEstimatorProxy = new PoseEstimatorProxy(poseEstimatorPoolSetting);

    public static void main(String[] args) throws FileNotFoundException {

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
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
