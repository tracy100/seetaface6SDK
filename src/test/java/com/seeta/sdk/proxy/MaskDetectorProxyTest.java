package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.GenderPredictorProxy;
import com.seeta.proxy.MaskDetectorProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class MaskDetectorProxyTest {


    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    public static String[] gender_predictor_cstas = {CSTA_PATH + "/gender_predictor.csta"};


    public static String fileName = "D:\\face\\image\\me\\00.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

//        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
//        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        SeetaConfSetting maskDetectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, gender_predictor_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        MaskDetectorProxy maskDetectorProxy = new MaskDetectorProxy(maskDetectorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
               // SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                MaskDetectorProxy.MaskItem detect = maskDetectorProxy.detect(image, seetaRect);
                System.out.println(detect.getMask());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
