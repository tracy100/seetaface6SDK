package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.AgePredictorProxy;
import com.seeta.proxy.EyeStateDetectorProxy;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

public class EyeStateDetectorProxyTest {

    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    public static String[] eye_cstas = {CSTA_PATH + "/eye_state.csta"};

    public static String TEST_PICT = "D:\\face\\image\\mask\\mask3.jpeg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        SeetaConfSetting eyeStateDetectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, eye_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

        EyeStateDetectorProxy eyeStateDetectorProxy = new EyeStateDetectorProxy(eyeStateDetectorPoolSetting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);

                EyeStateDetector.EYE_STATE[] detect = eyeStateDetectorProxy.detect(image, pointFS);
                System.out.println(Arrays.toString(detect));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
