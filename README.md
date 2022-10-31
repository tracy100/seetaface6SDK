# seetaface6SDK

#### 介绍
1. 本项目是基于seetaface6源码编译后，再编译seetaface6JNI源码得到的一个sdk项目
2. windows和linux环境自适应，直至aarch64环境（待测）
3. 支持GPU（需要低版本cuda）
4. 使用方便：只依赖jdk，打开就用。

#### 软件架构

1.  基于seetaface6 c++源码编译，基于JNI技术，通过编译c++ 得到dll和so。
2.  使用起来超级简单，打成jar包，导入项目就可以用了，不需要配置jni路径之类的。

#### 安装教程

1.  （必做）window10 环境需要安装 jdk8-jdk14任选。
2.  （必做）linux 环境需要安装 jdk8-jdk14任选。
3.  可以跟着test代码包里面的代码走一遍，了解使用方法，再自己引入自己项目中。
4.  本项目可以直接打包成jar，导入本地maven仓库或是私服,其他项目直接引用jar就可以了。
5.  只有windows10和centos7（centos8没试过，应该可以用）这两种so。
6.  GPU环境有点复杂，建议先试试CPU的，GPU环境的配置后面再提交说明。
7.  模型文件自己去下载了，这里不提供，下载地址请到官网去看，本项目也是官网源码编译而来。
8.  建了个QQ群：290690355
9.  **觉得好的是不是可以点个star**？

#### 演示真假人脸识别
1.  spoof为攻击人脸，real为真人脸
![Image text](https://gitee.com/crazy-of-pig/seeta-sdk-platform/raw/master/img/%E6%94%BB%E5%87%BB%E4%BA%BA%E8%84%B8%E6%A3%80%E6%B5%8B.jpg)

#### 测试代码
1.  代码注释详细，方便阅读
```java
public class AntiSpoofingTest {

    public static String CSTA_PATH = "D:\\face\\models";
    public static String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    /**
     * 初始化加载dll
     */
    static {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public static void main(String[] args) {
        //三个模型文件
        String[] detector_cstas = {CSTA_PATH + "/face_detector.csta"};
        // 这里传两个模型才能准确得出结果 （fas_first和fas_second）
        String[] fas_first = {CSTA_PATH + "/fas_first.csta"};
        String[] landmarker_cstas = {CSTA_PATH + "/face_landmarker_pts5.csta"};
        try {
            //人脸检测器
            FaceDetector detector = new FaceDetector(
                    new SeetaModelSetting(0, detector_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //关键点定位器face_landmarker_pts5 就是五个关键点，face_landmarker_pts68就是68个关键点，根据模型文件来的
            FaceLandmarker faceLandmarker = new FaceLandmarker(
                    new SeetaModelSetting(0, landmarker_cstas, SeetaDevice.SEETA_DEVICE_AUTO));
            //攻击人脸检测器
            FaceAntiSpoofing faceAntiSpoofing = new FaceAntiSpoofing(
                    new SeetaModelSetting(0, fas_first, SeetaDevice.SEETA_DEVICE_AUTO));

            SeetaImageData image = SeetafaceUtil.toSeetaImageData(TEST_PICT);
            SeetaRect[] detects = detector.Detect(image);
            for (SeetaRect seetaRect : detects) {
                //face_landmarker_pts5 根据这个来的
                SeetaPointF[] pointFS = new SeetaPointF[5];
                int[] ints = new int[5];
                faceLandmarker.mark(image, seetaRect, pointFS,ints);
                FaceAntiSpoofing.Status predict = faceAntiSpoofing.Predict(image, seetaRect, pointFS);
                System.out.println(predict);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 功能
1.  人脸检测和关键点定位
2.  人脸特征提取和对比
3.  人脸特征提取
4.  人脸特征对比
5.  活体检测
6.  人脸跟踪
7.  质量评估
8.  年龄识别
9.  性别识别
10.  口罩检测
11.  口罩人脸识别
12.  眼睛状态检测

#### 使用说明

模型介绍：模型自己去官网下载

| 模型                           | 模型说明                                                   | 备注                          |
| ------------------------------ | ---------------------------------------------------------- | ----------------------------- |
| face_recognizer.csta           | 高精度人脸识别人脸向量特征提取模型，建议阈值：0.62           | 返回1024长度向量特征          |
| face_recognizer_mask.csta      | 戴口罩人脸向量特征提取模型，建议阈值：0.48                   | 返回512长度向量特征           |
| face_recognizer_light.csta     | 轻量级人脸向量特征提取模型，建议阈值：0.55                   | 返回512长度向量特征           |
| age_predictor.csta             | 年龄预测模型                                               | 返回int[0]                    |
| face_landmarker_pts5.csta      | 5点人脸标识模型, 确定 两眼、两嘴角和鼻尖                     | SeetaPointF[] 即 x，y坐标数组 |
| face_landmarker_pts68.csta     | 68点人脸标识模型， 人脸68个特征点                           | SeetaPointF[] 即 x，y坐标数组 |
| pose_estimation.csta           | 人脸姿态评估                                               |                               |
| eye_state.csta                 | 眼睛状态评估                                               | 打开 关闭状态                 |
| face_detector.csta             | 人脸检测器，检测到的每个人脸位置，用矩形表示                 |                               |
| face_landmarker_mask_pts5.csta | 遮挡评估，判断的遮挡物为五个关键点，分别是左右眼中心、鼻尖和左右嘴角 | 1：遮挡，  0：没遮挡          |
| mask_detector.csta             | 口罩检测器                                                 | false:0.0089 或  true:0.985   |
| gender_predictor.csta          | 性别识别                                                   |                               |
| quality_lbn.csta               | 清晰度评估模型                                             |                               |
| fas_first.csta                 | 活体检测识别器 局部检测模型                                |                               |
| fas_second.csta                | 活体检测识别器 全局检测模型                                |                               |

#### 特技

1.  可以做人脸跟踪
2.  真假人脸判断
3.  年龄，性别判断
4.  质量检测
5.  后续会开放docker
6.  后续做1：N 用opensearch，能够达到10亿搜索量

#### QQ群
![Image text](https://gitee.com/crazy-of-pig/seeta-sdk-platform/raw/master/img/qq.jpg)
