package com.seeta.sdk.base;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.*;


/**
 * 深度学习的人脸姿态评估器。
 */
public class QualityOfPoseExTest {

    public static String TEST_PICT = "D:\\face\\image\\me\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {

        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(FileConstant.face_landmarker_pts68, SeetaDevice.SEETA_DEVICE_AUTO));
            QualityOfPoseEx qualityOfPoseEx = new QualityOfPoseEx(new SeetaModelSetting(FileConstant.pose_estimation, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);

            List<Map<String,Float>> list = new ArrayList<>();

            for (SeetaRect seetaRect : detects) {

                SeetaPointF[] pointFS = new SeetaPointF[68];
                faceLandmarker.mark(image, seetaRect, pointFS);

                float[] yaw = new float[1]; float[] pitch= new float[1]; float[] roll= new float[1];
                qualityOfPoseEx.check(image,seetaRect,pointFS,yaw,pitch,roll);

                Map<String,Float> map = new LinkedHashMap<>();

                map.put("yaw",yaw[0]);
                map.put("pitch",pitch[0]);
                map.put("roll",roll[0]);

                list.add(map);

            }

            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
