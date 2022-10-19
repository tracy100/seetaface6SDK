package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class LandmarkerTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\100.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_mask_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            BufferedImage imagea = SeetafaceUtil.toBufferedImage(TEST_PICT);
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
