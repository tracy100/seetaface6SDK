package com.seeta.sdk.proxy;

import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.FaceDetectorProxy;
import com.seeta.proxy.FaceLandmarkerProxy;
import com.seeta.proxy.GenderPredictorProxy;
import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.io.FileNotFoundException;

/**
 * 性别识别
 */
public class GenderPredictorProxyTest {

    //人脸识别检测器对象池配置，可以配置对象的个数哦
    public static SeetaConfSetting detectorPoolSetting;

    //人脸关键点定位器对象池配置
    public static SeetaConfSetting faceLandmarkerPoolSetting;

    //性别识别器对象池配置
    public static SeetaConfSetting genderPredictorPoolSetting;

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        try {
            detectorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            faceLandmarkerPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            genderPredictorPoolSetting = new SeetaConfSetting(
                    new SeetaModelSetting(FileConstant.gender_predictor, SeetaDevice.SEETA_DEVICE_AUTO));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
    public static FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);

    //人脸关键点定位器对象池代理 ， spring boot可以用FaceLandmarkerProxy来配置Bean
    public static FaceLandmarkerProxy faceLandmarkerProxy = new FaceLandmarkerProxy(faceLandmarkerPoolSetting);


    //性别识别器对象池代理 ， spring boot可以用GenderPredictorProxy来配置Bean
    public static GenderPredictorProxy genderPredictorProxy = new GenderPredictorProxy(genderPredictorPoolSetting);


    public static void main(String[] args) throws FileNotFoundException {

        try {
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(FileConstant.TEST_PICT);
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
