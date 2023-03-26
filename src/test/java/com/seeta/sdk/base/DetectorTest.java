package com.seeta.sdk.base;


import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.awt.image.BufferedImage;

/**
 * 人脸检测器 测试
 */
public class DetectorTest {

    /**
     * 初始化加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {

            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));
            BufferedImage image = SeetafaceUtil.toBufferedImage(FileConstant.TEST_PICT);
            //照片数据
            SeetaImageData imageData = SeetafaceUtil.toSeetaImageData(image);
            //检测到的人脸坐标
            SeetaRect[] detects = detector.Detect(imageData);
            int i = 0;
            for (SeetaRect rect : detects) {
                System.out.printf("第%s张人脸 x: %s, y: %s, width: %s, height: %s\n", i++, rect.x, rect.y, rect.width, rect.height);
                image = SeetafaceUtil.writeRect(image, rect);
            }
            SeetafaceUtil.show("人脸检测", image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
