package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

import java.util.*;


/**
 * 深度学习的人脸姿态评估器。
 */
public class QualityOfPoseExTest {


    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\11.jpg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
        //System.load("D:\\face\\seeta-sdk\\src\\main\\resources\\windows_x64\\QualityAssessor3JNI.dll");
    }

    public static void main(String[] args) {

        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        String[] pose_estimation_cstas = {CSTA_PATH + "/pose_estimation.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            FaceLandmarker faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            QualityOfPoseEx qualityOfPoseEx = new QualityOfPoseEx(new SeetaModelSetting(0, pose_estimation_cstas, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);

            List<Map<String,float[]>> list = new ArrayList<>();

            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[68];
                faceLandmarker.mark(image, seetaRect, pointFS);
                // public native void Detect(SeetaImageData imageData, SeetaPointF[] points, int[] light, int[] blur, int[] noise);

                //  private native void checkCore(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] yaw, float[] pitch, float[] roll);
                //QualityOfPoseEx.QualityLevel check = qualityOfPoseEx.check(image, seetaRect, pointFS, floats);
                float[] yaw = new float[1]; float[] pitch= new float[1]; float[] roll= new float[1];
                qualityOfPoseEx.check(image,seetaRect,pointFS,yaw,pitch,roll);

                Map<String,float[]> map = new LinkedHashMap<>();

                map.put("",yaw);
                map.put("",pitch);
                map.put("",roll);

                list.add(map);

            }

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
