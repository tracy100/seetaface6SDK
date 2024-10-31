# seetaface6SDK

### 1.介绍
1. 本项目是基于seetaface6源码和seetaface6JNI源码编译得到的一个sdk项目
2. windows和linux环境自适应。（支持aarch64环境--待测）,有小伙伴在国产麒麟aarch64服务器上跑成功过；
3. 支持GPU
4. jdk8-jdk14 都可用

### 2.模型文件
   
[模型文件1](https://pan.baidu.com/s/1LlXe2-YsUxQMe-MLzhQ2Aw) 提取码：ngne

[模型文件2](https://pan.baidu.com/s/1xjciq-lkzEBOZsTfVYAT9g) 提取码：t6j0

### 3.安装教程

1.  需要安装 jdk8-jdk14任选。
2.  可以跟着test代码包里面的代码走一遍，了解使用方法，再自己引入自己项目中。
3.  本项目可以直接打包成jar，导入本地maven仓库或是私服，或install,其他项目直接引用jar就可以了。
4.  支持windows和Linux。 arm后续看需求再支持 
5.  GPU环境需要安装显卡驱动和cuda（注意版本对应），我编译用的是512.15和11.6.2
6.  实际使用，可以参考另外一个项目：[spring boot,pgvector,mybatis,seetaface6SDK](https://gitee.com/crazy-of-pig/spring-boot-seetaface6.git)
7.  **觉得好的是不是可以点个 star**

### 4.功能
#### 1.  人脸检测
#### 2.  人脸识别
#### 3.  人脸跟踪
#### 4.  口罩检测
#### 5.  年龄估计
#### 6.  性别估计
#### 7.  特征点检测
#### 8.  眼睛状态检测
#### 9.  质量评估器
#### 10.  静默活体

### 5.使用说明

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


### 6.问题
#### 1.如何在其他项目中使用？
  将项目打成jar导入本地maven仓库，或手动上传到私服就可以引用。

#### 2.win7或win11缺少dll依赖？
  下载c++运行时依赖执行安装就好。 [下载链接](https://www.xitongzhijia.net/soft/234968.html)

#### 3.docker中运行少依赖libgomp.so这些？
  提供一个Dockerfile
 ```
# 使用一个基础镜像，这里以官方的Java镜像为例
FROM openjdk:8

# 设置作者信息
LABEL maintainer="linyc1993@outlook.com"

# 更新apt-get源，并安装libgomp1库
RUN apt-get update && \
    apt-get install -y --no-install-recommends libgomp1 && \
    rm -rf /var/lib/apt/lists/*

# 创建数据目录
RUN mkdir -p /data/app && mkdir -p /data/models

# 将JAR包复制到容器内的/data/app目录
COPY spring-boot-seetaface6-1.0.0.jar /data/app/spring-boot-seetaface6-1.0.0.jar

# 将模型文件复制到容器内的/data/models目录
COPY ./*.csta /data/models/

# 设置默认的工作目录
WORKDIR /data/app

# 定义运行时执行的命令，这里假设JAR包是可执行的
CMD ["java", "-jar", "spring-boot-seetaface6-1.0.0.jar"]
```

#### 4.在spring boot项目中配置bean？
建议使用封装好的对象池代理作为bean，配置如下：
```
    /**
     * 人脸检测器
     *
     * @return FaceDetectorProxy
     */
    @Bean
    public FaceDetectorProxy faceDetector() throws FileNotFoundException {
        //人脸识别检测器对象池配置
        SeetaConfSetting detectorPoolSetting = new SeetaConfSetting(
                new SeetaModelSetting(0, modelPath.getFace_detector(), SeetaDevice.SEETA_DEVICE_GPU));
        //人脸检测器对象池代理 ， spring boot可以用FaceDetectorProxy来配置Bean
        FaceDetectorProxy faceDetectorProxy = new FaceDetectorProxy(detectorPoolSetting);
        return faceDetectorProxy;
    }

```
#### 5.实在解决不了？
加q: 2284901326