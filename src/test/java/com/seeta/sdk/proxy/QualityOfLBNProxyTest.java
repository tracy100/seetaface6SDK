package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.QualityOfLBNProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class QualityOfLBNProxyTest {


    public static String CSTA_PATH = "D:\\face\\models";

    public static String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

    public static String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts68.csta"};

    public static String[]  qualityOfLBN_cstas = {CSTA_PATH + "/quality_lbn.csta"};


    public static String fileName = "D:\\face\\image\\me\\00.jpg";


    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

        SeetaConfSetting faceLandmarkerPoolSetting = new SeetaConfSetting(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);

        SeetaConfSetting setting = new SeetaConfSetting(new SeetaModelSetting(0, qualityOfLBN_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
        QualityOfLBNProxy qualityOfLBNProxy = new QualityOfLBNProxy(setting);

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                SeetaPointF[] pointFS = faceLandmarkerProxy.mark(image, seetaRect);
                QualityOfLBNProxy.LBNClass detect = qualityOfLBNProxy.detect(image, pointFS);

                System.out.println(detect.getBlurstate() + " === " + detect.getLightstate() + " === " + detect.getNoisestate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//BLUR === DARK === HAVENOISE
}
