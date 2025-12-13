package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.Arrays;

/**
 * 深度学习的人脸清晰度评估器 68点
 */
public class QualityOfLBNTest {

    public static String TEST_PICT = "E:\\face\\image\\me\\33.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);

    }

    public static void main(String[] args) {

        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            //人脸关键点定位器
            FaceLandmarker faceLandmarker68 = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts68, SeetaDevice.SEETA_DEVICE_AUTO));

            QualityOfLBN qualityOfLBN = new QualityOfLBN(new SeetaModelSetting(FileConstant.quality_lbn, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {

                System.out.println(faceLandmarker68.number());
                SeetaPointF[] pointFS = new SeetaPointF[faceLandmarker68.number()];
                faceLandmarker68.mark(image, seetaRect, pointFS);
               // public native void Detect(SeetaImageData imageData, SeetaPointF[] points, int[] light, int[] blur, int[] noise);
                int[] light = new int[1];

                int[] blur = new int[1];

                int[] noise = new int[1];

                qualityOfLBN.Detect(image,pointFS,light,blur,noise);

                System.out.println(Arrays.toString(light));
                System.out.println(Arrays.toString(blur));
                System.out.println(Arrays.toString(noise));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
