package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.FaceRecognizerProxy;
import com.seeta.proxy.QualityOfLBNProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;

public class QualityOfLBNProxyTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;


    public static  SeetaConfSetting setting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        detectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

        faceLandmarkerPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(FileConstant.face_landmarker_pts68, SeetaDevice.SEETA_DEVICE_AUTO));

        setting = new SeetaConfSetting(new SeetaModelSetting(FileConstant.quality_lbn, SeetaDevice.SEETA_DEVICE_AUTO));

    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarker68Proxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);


    public static  QualityOfLBNProxy qualityOfLBNProxy = new QualityOfLBNProxy(setting);

    public static void main(String[] args) throws FileNotFoundException {

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
            SeetaRect[] detects = faceDetectorProxy.detect(image);
            for (SeetaRect seetaRect : detects) {
                SeetaPointF[] pointFS = faceLandmarker68Proxy.mark(image, seetaRect);
                QualityOfLBNProxy.LBNClass detect = qualityOfLBNProxy.detect(image, pointFS);

                System.out.println(detect.getBlurstate() + " === " + detect.getLightstate() + " === " + detect.getNoisestate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//BLUR === DARK === HAVENOISE
}
