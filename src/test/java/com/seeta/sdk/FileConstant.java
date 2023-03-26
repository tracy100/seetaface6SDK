package com.seeta.sdk;

public class FileConstant {


    // 模型文件目录
    public static final String CSTA_PATH = "D:\\face\\models";

    //测试照片
    public static final String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    //模型文件
    public static final String[] face_detector = {CSTA_PATH + "/face_detector.csta"};

    //年龄
    public static final String[] age_predictor = {CSTA_PATH + "/age_predictor.csta"};

    //五点
    public static final String[] face_landmarker_pts5 = {CSTA_PATH + "/face_landmarker_pts5.csta"};

    //68点
    public static final String[] face_landmarker_pts68 = {CSTA_PATH + "/face_landmarker_pts68.csta"};

    // 攻击人脸检测
    public static final String[] fas_arr = {CSTA_PATH + "/fas_first.csta", CSTA_PATH + "/fas_second.csta"};

    //眼睛状态检测的模型文件
    public static final String[] eye_state = {CSTA_PATH + "/eye_state.csta"};

    //人脸向量特征提取和对比模型
    public static final String[] face_recognizer = {CSTA_PATH + "/face_recognizer.csta"};

    //性别
    public static final String[] gender_predictor = {CSTA_PATH + "/gender_predictor.csta"};

    //姿态
    public static final String[] pose_estimation = {CSTA_PATH + "/pose_estimation.csta"};

    //清晰度
    public static final String[] quality_lbn = {CSTA_PATH + "/quality_lbn.csta"};


    //口罩模型文件
    public static final  String[] mask_cstas = {CSTA_PATH + "/mask_detector.csta"};


}
