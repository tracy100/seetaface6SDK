package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * 人脸关键点定位器
 */
public class LandmarkerTest {


    /**
     * 加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts5, SeetaDevice.SEETA_DEVICE_AUTO));

            BufferedImage imagea = SeetafaceUtil.toBufferedImage(FileConstant.TEST_PICT);
            SeetaImageData image = SeetafaceUtil.toSeetaImageData(imagea);
            SeetaRect[] detects = detector.Detect(image);

            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                int[] masks = new int[5];
                faceLandmarker.mark(image, seetaRect, pointFS, masks);
                System.out.println(Arrays.toString(masks));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
