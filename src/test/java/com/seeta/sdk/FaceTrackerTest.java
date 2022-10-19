package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class FaceTrackerTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {


        String face_detector = CSTA_PATH + "/face_detector.csta";
        try {

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);


            FaceTracker faceTracker = new FaceTracker( face_detector,image.width,image.height);
            SeetaTrackingFaceInfo[] track = faceTracker.Track(image);
            System.out.println(track);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
