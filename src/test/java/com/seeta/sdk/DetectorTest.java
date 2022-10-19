package com.seeta.sdk;


import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.awt.image.BufferedImage;

public class DetectorTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "E:\\face-search\\face-search-test\\src\\main\\resources\\image\\validate\\search\\460f29423cf109d10fe262fb3cff685f.jpeg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(-1, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            BufferedImage image = SeetafaceUtil.toBufferedImage(TEST_PICT);

            image = SeetafaceUtil.resize(image, 480, 320);

            SeetaImageData imageData = SeetafaceUtil.toSeetaImageData(image);

            SeetaRect[] detects = detector.Detect(imageData);
            int i = 1;
            for (SeetaRect rect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[68];

                System.out.printf("第%s张人脸 x: %s, y: %s, width: %s, height: %s\n", i++, rect.x, rect.y, rect.width, rect.height);
                image = SeetafaceUtil.writeRect(image, rect);

            }
            SeetafaceUtil.show("人脸检测", image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
