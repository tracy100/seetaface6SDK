package com.seeta.sdk;

import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;

public class MemoryTest {
    public static FaceDetector detector = null;

    public static FaceLandmarker faceLandmarker = null;
    public static FaceRecognizer faceRecognizer = null;
    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "E:\\face-search\\face-search-test\\src\\main\\resources\\image\\validate\\search\\460f29423cf109d10fe262fb3cff685f.jpeg";

    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {
// 开始时间
        long stime = System.currentTimeMillis();


        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};

        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};

        String[] recognizer_cstas = {CSTA_PATH + "/face_recognizer_mask.csta"};
        try {

            detector = new FaceDetector(new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_GPU));


            faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_GPU));

            faceRecognizer = new FaceRecognizer(new SeetaModelSetting(0, recognizer_cstas, SeetaDevice.SEETA_DEVICE_GPU));

            String fileName = "D:\\face\\image\\me\\00.jpg";
            String fileName2 = "D:\\face\\image\\me\\11.jpg";
            for (int i = 0; i < 100; i++) {
                float[] features1 = extract(fileName);
                float[] features2 = extract(fileName2);
                if (features1 != null && features2 != null) {
                    float calculateSimilarity = faceRecognizer.CalculateSimilarity(features1, features2);
                    System.out.printf("第%s个,相似度:%f\n", i, calculateSimilarity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 结束时间
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }

    /**
     * 获取特征数组
     *
     * @author YaoCai Lin
     * @time 2020年7月15日 下午12:10:56
     */
    private static float[] extract(String fileName) {
        SeetaImageData image = SeetafaceUtil.toSeetaImageData(fileName);

        SeetaRect[] detects = detector.Detect(image);
        for (SeetaRect seetaRect : detects) {

            SeetaPointF[] pointFS = new SeetaPointF[5];
            int[] masks = new int[5];
            faceLandmarker.mark(image, seetaRect, pointFS, masks);
            float[] features = new float[512];

            faceRecognizer.Extract(image, pointFS, features);
            return features;
        }
        return null;
    }

}
