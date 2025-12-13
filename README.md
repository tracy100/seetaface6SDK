# SeetaFace6 Java SDK

<div align="center">

[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-8%20--%2014-blue.svg)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux-lightgrey.svg)](#)
[![ARM](https://img.shields.io/badge/ARM-aarch64%20%7C%20arm-green.svg)](#)

**基于SeetaFace6的Java语言SDK封装，提供完整的人脸识别解决方案**

[项目简介](#1-项目简介) • [核心功能](#2-核心功能) • [快速开始](#3-快速开始) • [API文档](#4-api文档) • [模型说明](#5-模型说明) • [最佳实践](#6-最佳实践) • [常见问题](#7-常见问题) • [技术支持](#8-技术支持)

</div>

---

## 1. 项目简介

### 1.1 项目概述

本项目是基于**SeetaFace6**开源人脸识别引擎的Java SDK封装，提供了完整的Java API接口，支持：

- ✅ **跨平台支持**：Windows 7/10/11, Linux (x86_64, aarch64, arm)
- ✅ **多架构兼容**：amd64, aarch64, arm
- ✅ **GPU加速**：支持CUDA (需CUDA 11.6.2)
- ✅ **JDK兼容**：JDK 8-14
- ✅ **对象池管理**：基于Apache Commons Pool2的线程安全对象池
- ✅ **三层架构**：SDK → Proxy → Pool 高性能架构模式

### 1.2 技术架构

```
┌─────────────────────────────────────┐
│           应用层 (Application)         │
│     Spring Boot / 其他Java应用        │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│          代理层 (Proxy Layer)         │
│     简化API调用，自动对象池管理         │
│  FaceDetectorProxy                   │
│  FaceRecognizerProxy                 │
│  FaceLandmarkerProxy                 │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│          池层 (Pool Layer)           │
│     线程安全的对象池管理               │
│  FaceDetectorPool                    │
│  FaceRecognizerPool                  │
│  FaceLandmarkerPool                  │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│         SDK层 (SDK Layer)            │
│      原生JNI接口封装                  │
│  FaceDetector                        │
│  FaceRecognizer                      │
│  FaceLandmarker                      │
└─────────────────────────────────────┘
```

---

## 2. 核心功能

| 功能模块 | 功能描述 | 核心类 | 支持模型 |
|---------|---------|--------|----------|
| **人脸检测** | 检测图像中的人脸位置，返回矩形框坐标 | `FaceDetector` | face_detector.csta |
| **人脸识别** | 提取人脸特征向量，计算相似度 | `FaceRecognizer` | face_recognizer.csta<br/>face_recognizer_mask.csta<br/>face_recognizer_light.csta |
| **人脸关键点** | 定位人脸5点或68点特征点 | `FaceLandmarker` | face_landmarker_pts5.csta<br/>face_landmarker_pts68.csta |
| **人脸跟踪** | 实时跟踪视频中的人脸 | `FaceTracker` | face_detector.csta |
| **年龄估计** | 预测人脸年龄 | `AgePredictor` | age_predictor.csta |
| **性别识别** | 判断人脸性别 | `GenderPredictor` | gender_predictor.csta |
| **口罩检测** | 检测是否佩戴口罩 | `MaskDetector` | mask_detector.csta |
| **眼睛状态** | 检测眼睛睁开/闭合状态 | `EyeStateDetector` | eye_state.csta |
| **姿态估计** | 评估人脸姿态角度 | `PoseEstimator` | pose_estimation.csta |
| **质量评估** | 评估图像质量（清晰度、亮度等） | `QualityOf*` | quality_lbn.csta |
| **活体检测** | 防伪检测，识别真实人脸 | `FaceAntiSpoofing` | fas_first.csta<br/>fas_second.csta |
| **遮挡评估** | 评估人脸遮挡情况 | `LandmarkerMask` | face_landmarker_mask_pts5.csta |

---

## 3. 快速开始

### 3.1 环境要求

#### 3.1.1 软件环境
- **JDK**: 8 - 14
- **Maven**: 3.6+
- **OS**: Windows 7/10/11 或 Linux

#### 3.1.2 GPU支持 (可选)
- **CUDA**: 11.6.2
- **Driver**: 512.15
- **GPU**: NVIDIA CUDA-enabled GPU

### 3.2 安装步骤

#### 步骤1：下载模型文件

```bash
# 模型文件下载链接
# 链接1: https://pan.baidu.com/s/1LlXe2-YsUxQMe-MLzhQ2Aw  提取码：ngne
# 链接2: https://pan.baidu.com/s/1xjciq-lkzEBOZsTfVYAT9g  提取码：t6j0

# 解压后放置到指定目录，例如：
D:\face\models\
├── face_detector.csta
├── face_recognizer.csta
├── face_landmarker_pts5.csta
└── ... (其他模型文件)
```

#### 步骤2：配置模型路径

编辑 `src/test/java/com/seeta/sdk/FileConstant.java`：

```java
public class FileConstant {
    // 模型文件路径
    public static final String CSTA_PATH = "D:\\face\\models";

    // 测试图片路径
    public static final String TEST_PICT = "D:\\face\\image\\me\\00.jpg";

    // 具体模型文件
    public static final String face_detector = CSTA_PATH + "\\face_detector.csta";
    public static final String face_recognizer = CSTA_PATH + "\\face_recognizer.csta";
    public static final String face_landmarker_pts5 = CSTA_PATH + "\\face_landmarker_pts5.csta";
    // ... 其他模型路径
}
```

#### 步骤3：编译项目

```bash
# 编译项目
mvn clean package

# 安装到本地仓库
mvn install
```

### 3.3 第一个示例

#### 3.3.1 直接使用SDK

```java
import com.seeta.sdk.*;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        // 1. 加载本地库
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_CPU);

        // 2. 初始化模型
        SeetaModelSetting setting = new SeetaModelSetting(
            new String[]{FileConstant.face_detector},
            SeetaDevice.SEETA_DEVICE_CPU
        );

        // 3. 创建检测器
        FaceDetector detector = new FaceDetector(setting);

        // 4. 加载图片
        SeetaImageData image = SeetafaceUtil.toSeetaImageData("path/to/image.jpg");

        // 5. 检测人脸
        SeetaRect[] faces = detector.Detect(image);
        System.out.println("检测到 " + faces.length + " 个人脸");

        // 6. 释放资源
        detector.dispose();
    }
}
```

#### 3.3.2 使用Proxy层 (推荐)

```java
import com.seeta.pool.SeetaConfSetting;
import com.seeta.proxy.*;

public class ProxyExample {
    // 配置对象池
    static SeetaConfSetting detectorSetting = new SeetaConfSetting(
        new SeetaModelSetting(FileConstant.face_detector, SeetaDevice.SEETA_DEVICE_CPU)
    );

    // 创建代理
    static FaceDetectorProxy detectorProxy = new FaceDetectorProxy(detectorSetting);

    public static void main(String[] args) throws Exception {
        LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_CPU);

        SeetaImageData image = SeetafaceUtil.toSeetaImageData("path/to/image.jpg");

        // 直接调用，无需手动管理对象生命周期
        SeetaRect[] faces = detectorProxy.detect(image);
        System.out.println("检测到 " + faces.length + " 个人脸");
    }
}
```

#### 3.3.3 Spring Boot集成

```java
@Configuration
public class SeetaFaceConfig {

    @Value("${seeta.model.path}")
    private String modelPath;

    @Bean
    public FaceDetectorProxy faceDetector() throws Exception {
        SeetaConfSetting setting = new SeetaConfSetting(
            new SeetaModelSetting(modelPath + "/face_detector.csta", SeetaDevice.SEETA_DEVICE_CPU)
        );
        return new FaceDetectorProxy(setting);
    }

    @Bean
    public FaceRecognizerProxy faceRecognizer() throws Exception {
        SeetaConfSetting setting = new SeetaConfSetting(
            new SeetaModelSetting(modelPath + "/face_recognizer.csta", SeetaDevice.SEETA_DEVICE_CPU)
        );
        return new FaceRecognizerProxy(setting);
    }

    @Bean
    public FaceLandmarkerProxy faceLandmarker() throws Exception {
        SeetaConfSetting setting = new SeetaConfSetting(
            new SeetaModelSetting(modelPath + "/face_landmarker_pts5.csta", SeetaDevice.SEETA_DEVICE_CPU)
        );
        return new FaceLandmarkerProxy(setting);
    }
}
```

---

## 4. API文档

### 4.1 核心类说明

#### 4.1.1 SeetaDevice - 设备枚举

```java
public enum SeetaDevice {
    SEETA_DEVICE_AUTO,    // 自动检测
    SEETA_DEVICE_CPU,     // CPU设备
    SEETA_DEVICE_GPU      // GPU设备
}
```

#### 4.1.2 SeetaModelSetting - 模型配置

```java
// 构造函数
public SeetaModelSetting(int id, String[] models, SeetaDevice dev)

// 示例：CPU模式
SeetaModelSetting cpuSetting = new SeetaModelSetting(
    0,
    new String[]{"model.csta"},
    SeetaDevice.SEETA_DEVICE_CPU
);

// 示例：GPU模式
SeetaModelSetting gpuSetting = new SeetaModelSetting(
    0,  // GPU ID
    new String[]{"model.csta"},
    SeetaDevice.SEETA_DEVICE_GPU
);
```

#### 4.1.3 SeetaImageData - 图像数据

```java
public class SeetaImageData {
    public int width;      // 图像宽度
    public int height;     // 图像高度
    public int channels;   // 图像通道数 (1=灰度, 3=彩色)
    public byte[] data;    // 像素数据
}

// 从文件创建
SeetaImageData image = SeetafaceUtil.toSeetaImageData("image.jpg");
```

#### 4.1.4 SeetaRect - 人脸矩形框

```java
public class SeetaRect {
    public int x;      // 左上角x坐标
    public int y;      // 左上角y坐标
    public int width;  // 宽度
    public int height; // 高度
}
```

#### 4.1.5 SeetaPointF - 特征点坐标

```java
public class SeetaPointF {
    public double x;   // x坐标
    public double y;   // y坐标
}
```

### 4.2 功能模块API

#### 4.2.1 人脸检测 - FaceDetector

```java
public class FaceDetector {
    // 构造方法
    public FaceDetector(SeetaModelSetting setting) throws Exception

    // 检测人脸
    public SeetaRect[] Detect(SeetaImageData image)

    // 设置属性
    public void set(Property property, double value)

    // 获取属性
    public double get(Property property)

    // 释放资源
    public void dispose()
}

// Property枚举
public enum Property {
    PROPERTY_MIN_FACE_SIZE(0),        // 最小人脸尺寸
    PROPERTY_THRESHOLD(1),            // 检测阈值
    PROPERTY_MAX_IMAGE_WIDTH(2),      // 最大图像宽度
    PROPERTY_MAX_IMAGE_HEIGHT(3),     // 最大图像高度
    PROPERTY_NUMBER_THREADS(4),       // 线程数
    PROPERTY_ARM_CPU_MODE(0x101)      // ARM CPU模式
}

// 使用示例
FaceDetector detector = new FaceDetector(setting);
detector.set(FaceDetector.Property.PROPERTY_MIN_FACE_SIZE, 50);
detector.set(FaceDetector.Property.PROPERTY_THRESHOLD, 0.7);
SeetaRect[] faces = detector.Detect(image);
```

#### 4.2.2 人脸识别 - FaceRecognizer

```java
public class FaceRecognizer {
    // 提取特征
    public boolean Extract(SeetaImageData image, SeetaPointF[] points, float[] features)

    // 计算相似度
    public float CalculateSimilarity(float[] features1, float[] features2)

    // 裁剪人脸
    public boolean CropFaceV2(SeetaImageData image, SeetaPointF[] points, SeetaImageData face)

    // 获取特征向量长度
    public int GetExtractFeatureSize()

    // 获取裁剪人脸尺寸
    public int GetCropFaceWidthV2()
    public int GetCropFaceHeightV2()
    public int GetCropFaceChannelsV2()
}

// 使用示例
FaceRecognizer recognizer = new FaceRecognizer(setting);
float[] features1 = new float[recognizer.GetExtractFeatureSize()];
float[] features2 = new float[recognizer.GetExtractFeatureSize()];

recognizer.Extract(image1, points1, features1);
recognizer.Extract(image2, points2, features2);

float similarity = recognizer.CalculateSimilarity(features1, features2);
System.out.println("相似度: " + similarity);

// 相似度阈值判断
if (similarity > 0.62) {
    System.out.println("同一个人");
} else {
    System.out.println("不同的人");
}
```

#### 4.2.3 人脸关键点 - FaceLandmarker

```java
public class FaceLandmarker {
    // 构造方法
    public FaceLandmarker(SeetaModelSetting setting)

    // 获取关键点数量
    public int number()

    // 标记关键点
    public void mark(SeetaImageData imageData, SeetaRect seetaRect, SeetaPointF[] pointFS)

    // 标记关键点（含遮挡信息）
    public void mark(SeetaImageData imageData, SeetaRect seetaRect, SeetaPointF[] pointFS, int[] masks)

    // 释放资源
    public void dispose()
}

// 使用示例
FaceLandmarker landmarker = new FaceLandmarker(setting);
int pointCount = landmarker.number();  // 5或68
SeetaPointF[] points = new SeetaPointF[pointCount];
landmarker.mark(image, faceRect, points);
```

#### 4.2.4 年龄预测 - AgePredictor

```java
public class AgePredictor {
    // 直接预测年龄（需要先裁剪）
    public boolean PredictAge(SeetaImageData face, int[] age)

    // 预测年龄（含裁剪）
    public boolean PredictAgeWithCrop(SeetaImageData image, SeetaPointF[] points, int[] age)

    // Java风格方法
    public Integer predictAgeWithCrop(SeetaImageData image, SeetaPointF[] points)

    // 获取裁剪人脸尺寸
    public int GetCropFaceWidth()
    public int GetCropFaceHeight()
    public int GetCropFaceChannels()

    // 裁剪人脸
    public boolean CropFace(SeetaImageData image, SeetaPointF[] points, SeetaImageData face)
}

// 使用示例
AgePredictor agePredictor = new AgePredictor(setting);
Integer age = agePredictor.predictAgeWithCrop(image, points);
System.out.println("预测年龄: " + age);
```

#### 4.2.5 性别识别 - GenderPredictor

```java
public class GenderPredictor {
    public GenderPredictor(SeetaModelSetting setting)

    public boolean PredictGender(SeetaImageData face, int[] gender)

    public boolean PredictGenderWithCrop(SeetaImageData image, SeetaPointF[] points, int[] gender)

    public Integer predictGenderWithCrop(SeetaImageData image, SeetaPointF[] points)

    public boolean CropFace(SeetaImageData image, SeetaPointF[] points, SeetaImageData face)

    public int GetCropFaceWidth()
    public int GetCropFaceHeight()
    public int GetCropFaceChannels()
}

// gender值：0=女性, 1=男性
Integer gender = genderPredictor.predictGenderWithCrop(image, points);
System.out.println("预测性别: " + (gender == 1 ? "男性" : "女性"));
```

#### 4.2.6 口罩检测 - MaskDetector

```java
public class MaskDetector {
    public MaskDetector(SeetaModelSetting setting) throws Exception

    // 检测口罩
    // @param score 置信度（返回），score[0] > 0.5 表示戴口罩
    public boolean detect(SeetaImageData imageData, SeetaRect face, float[] score)
}

// 使用示例
MaskDetector maskDetector = new MaskDetector(setting);
float[] score = new float[1];
boolean hasMask = maskDetector.detect(image, faceRect, score);
System.out.println("置信度: " + score[0]);
System.out.println(hasMask ? "戴口罩" : "未戴口罩");
```

#### 4.2.7 眼睛状态检测 - EyeStateDetector

```java
public class EyeStateDetector {
    public EyeStateDetector(SeetaModelSetting setting)

    // 检测眼睛状态
    // @param eyeStates 返回眼睛状态数组，1=睁开, 0=闭合
    public boolean detect(SeetaImageData image, SeetaRect face, int[] eyeStates)
}

// 使用示例
EyeStateDetector eyeDetector = new EyeStateDetector(setting);
int[] eyeStates = new int[2];  // 左眼、右眼
eyeDetector.detect(image, faceRect, eyeStates);
System.out.println("左眼状态: " + (eyeStates[0] == 1 ? "睁开" : "闭合"));
System.out.println("右眼状态: " + (eyeStates[1] == 1 ? "睁开" : "闭合"));
```

#### 4.2.8 活体检测 - FaceAntiSpoofing

```java
public class FaceAntiSpoofing {
    public FaceAntiSpoofing(SeetaModelSetting setting)

    // 活体检测
    // @param status 返回状态，0=真实人脸, 1=攻击人脸, 2=不确定
    // @param score 置信度
    public int detect(SeetaImageData image, SeetaRect face, float[] score)
}

// 使用示例
FaceAntiSpoofing antiSpoofing = new FaceAntiSpoofing(setting);
float[] score = new float[1];
int status = antiSpoofing.detect(image, faceRect, score);

switch (status) {
    case 0:
        System.out.println("真实人脸 (置信度: " + score[0] + ")");
        break;
    case 1:
        System.out.println("攻击人脸 (置信度: " + score[0] + ")");
        break;
    case 2:
        System.out.println("不确定 (置信度: " + score[0] + ")");
        break;
}
```

### 4.3 Proxy层API

Proxy层提供简化API，自动管理对象池：

#### 4.3.1 FaceDetectorProxy

```java
public class FaceDetectorProxy {
    public FaceDetectorProxy(SeetaConfSetting setting)

    public SeetaRect[] detect(SeetaImageData image)
}
```

#### 4.3.2 FaceRecognizerProxy

```java
public class FaceRecognizerProxy {
    public FaceRecognizerProxy(SeetaConfSetting setting)

    public float[] extract(SeetaImageData image, SeetaPointF[] points)

    public float cosineSimilarity(float[] features1, float[] features2)
}
```

#### 4.3.3 FaceLandmarkerProxy

```java
public class FaceLandmarkerProxy {
    public FaceLandmarkerProxy(SeetaConfSetting setting)

    public SeetaPointF[] mark(SeetaImageData image, SeetaRect face)
}
```

---

## 5. 模型说明

### 5.1 模型文件列表

| 模型文件名 | 功能描述 | 特征维度 | 推荐阈值 | 使用场景 |
|-----------|---------|---------|----------|----------|
| **face_detector.csta** | 人脸检测器 | - | - | 人脸检测、人脸跟踪 |
| **face_recognizer.csta** | 高精度人脸识别 | 1024维 | 0.62 | 标准人脸识别 |
| **face_recognizer_mask.csta** | 戴口罩人脸识别 | 512维 | 0.48 | 口罩场景识别 |
| **face_recognizer_light.csta** | 轻量级人脸识别 | 512维 | 0.55 | 资源受限环境 |
| **face_landmarker_pts5.csta** | 5点关键点检测 | - | - | 基础关键点定位 |
| **face_landmarker_pts68.csta** | 68点关键点检测 | - | - | 精细化关键点 |
| **age_predictor.csta** | 年龄预测 | - | - | 年龄分析 |
| **gender_predictor.csta** | 性别识别 | - | - | 性别分析 |
| **eye_state.csta** | 眼睛状态检测 | - | - | 疲劳检测 |
| **mask_detector.csta** | 口罩检测 | - | 0.5 | 口罩识别 |
| **pose_estimation.csta** | 姿态估计 | - | - | 角度分析 |
| **quality_lbn.csta** | 图像质量评估 | - | - | 质量评分 |
| **fas_first.csta** | 活体检测-局部 | - | - | 活体检测 |
| **fas_second.csta** | 活体检测-全局 | - | - | 活体检测 |
| **face_landmarker_mask_pts5.csta** | 遮挡评估 | - | - | 遮挡分析 |

### 5.2 模型下载

```
# 百度网盘链接
链接1: https://pan.baidu.com/s/1LlXe2-YsUxQMe-MLzhQ2Aw  提取码：ngne
链接2: https://pan.baidu.com/s/1xjciq-lkzEBOZsTfVYAT9g  提取码：t6j0
```

### 5.3 模型配置示例

```java
// 基础人脸识别模型配置
SeetaModelSetting recognizerSetting = new SeetaModelSetting(
    new String[]{"D:/models/face_recognizer.csta"},
    SeetaDevice.SEETA_DEVICE_CPU
);

// GPU加速配置
SeetaModelSetting gpuSetting = new SeetaModelSetting(
    0,  // GPU ID
    new String[]{"D:/models/face_recognizer.csta"},
    SeetaDevice.SEETA_DEVICE_GPU
);

// 多模型配置（某些模型需要多个文件）
SeetaModelSetting antiSpoofingSetting = new SeetaModelSetting(
    new String[]{
        "D:/models/fas_first.csta",
        "D:/models/fas_second.csta"
    },
    SeetaDevice.SEETA_DEVICE_CPU
);
```

### 5.4 阈值建议

| 应用场景 | 建议阈值 | 说明 |
|---------|---------|------|
| **安防门禁** | 0.70 | 高安全性，宁可误拒不可误受 |
| **考勤系统** | 0.65 | 平衡安全性和便捷性 |
| **手机解锁** | 0.60 | 便捷性优先 |
| **戴口罩场景** | 0.48 | 针对口罩识别模型 |
| **活体检测** | 0.80 | 高安全性要求 |

---

## 6. 最佳实践

### 6.1 对象池配置

#### 6.1.1 SeetaConfSetting配置

```java
public class SeetaConfSetting {
    // 构造函数
    public SeetaConfSetting(SeetaModelSetting modelSetting)

    // 设置对象池大小
    public void setMaxTotal(int maxTotal)

    // 设置最大空闲对象数
    public void setMaxIdle(int maxIdle)

    // 设置最小空闲对象数
    public void setMinIdle(int minIdle)
}

// 示例：高性能配置
SeetaConfSetting setting = new SeetaConfSetting(
    new SeetaModelSetting(modelPath, SeetaDevice.SEETA_DEVICE_CPU)
);
setting.setMaxTotal(20);      // 最大对象数
setting.setMaxIdle(10);       // 最大空闲数
setting.setMinIdle(5);        // 最小空闲数
```

#### 6.1.2 推荐配置

| 场景 | CPU核心数 | 对象池大小 | 线程池大小 |
|------|----------|-----------|-----------|
| **开发测试** | - | 4 | 4 |
| **单线程应用** | - | 2-4 | 2-4 |
| **多线程应用** | N核 | N*2 | N*2 |

### 6.2 性能优化

#### 6.2.1 图像预处理

```java
// 推荐图像尺寸：640x480 到 1920x1080
// 过大图像影响性能，过小图像影响精度

// 调整图像大小
public SeetaImageData resizeImage(SeetaImageData src, int targetWidth, int targetHeight) {
    // 使用Java图像处理库调整大小
    // ...
    return resizedImage;
}
```

#### 6.2.2 批量处理

```java
// 使用线程池批量处理
ExecutorService executor = Executors.newFixedThreadPool(8);

List<Future<Result>> futures = new ArrayList<>();
for (ImageData image : imageList) {
    futures.add(executor.submit(() -> processImage(image)));
}

// 等待所有任务完成
for (Future<Result> future : futures) {
    Result result = future.get();
}

executor.shutdown();
```

### 6.3 线程安全

所有Proxy层类都是线程安全的，可以在多线程环境中使用：

```java
// 线程安全的Proxy
public class ThreadSafeExample {
    static FaceDetectorProxy detector = new FaceDetectorProxy(setting);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                SeetaRect[] faces = detector.detect(image);
                // 处理结果
            });
        }

        executor.shutdown();
    }
}
```

### 6.4 完整示例：人脸识别流程

```java
public class FaceRecognitionPipeline {
    private FaceDetectorProxy detector;
    private FaceLandmarkerProxy landmarker;
    private FaceRecognizerProxy recognizer;

    public FaceRecognitionPipeline(SeetaConfSetting setting) {
        this.detector = new FaceDetectorProxy(setting);
        this.landmarker = new FaceLandmarkerProxy(setting);
        this.recognizer = new FaceRecognizerProxy(setting);
    }

    /**
     * 完整的人脸识别流程
     */
    public RecognitionResult recognize(SeetaImageData image) {
        RecognitionResult result = new RecognitionResult();

        // 1. 人脸检测
        SeetaRect[] faces = detector.detect(image);
        if (faces == null || faces.length == 0) {
            result.setSuccess(false);
            result.setMessage("未检测到人脸");
            return result;
        }

        // 取第一个检测到的人脸
        SeetaRect face = faces[0];

        // 2. 关键点定位
        SeetaPointF[] points = landmarker.mark(image, face);
        if (points == null || points.length == 0) {
            result.setSuccess(false);
            result.setMessage("关键点定位失败");
            return result;
        }

        // 3. 特征提取
        float[] features = recognizer.extract(image, points);
        if (features == null || features.length == 0) {
            result.setSuccess(false);
            result.setMessage("特征提取失败");
            return result;
        }

        // 4. 构建结果
        result.setSuccess(true);
        result.setFace(face);
        result.setPoints(points);
        result.setFeatures(features);
        result.setFeatureLength(features.length);

        return result;
    }

    /**
     * 计算两张人脸的相似度
     */
    public float compare(float[] features1, float[] features2) {
        return recognizer.cosineSimilarity(features1, features2);
    }

    /**
     * 与数据库中的人脸特征对比
     */
    public String compareWithDatabase(float[] queryFeatures, Map<String, float[]> database) {
        float bestSimilarity = 0;
        String bestMatchId = null;

        for (Map.Entry<String, float[]> entry : database.entrySet()) {
            float similarity = compare(queryFeatures, entry.getValue());
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestMatchId = entry.getKey();
            }
        }

        // 阈值判断
        if (bestSimilarity > 0.62) {
            return bestMatchId;
        } else {
            return null;  // 未找到匹配
        }
    }
}
```

### 6.5 内存管理

#### 6.5.1 及时释放资源

```java
// SDK层需要手动释放
FaceDetector detector = new FaceDetector(setting);
try {
    SeetaRect[] faces = detector.Detect(image);
    // 处理结果
} finally {
    detector.dispose();  // 重要：释放本地资源
}

// Proxy层自动管理
try (FaceDetectorProxy proxy = new FaceDetectorProxy(setting)) {
    SeetaRect[] faces = proxy.detect(image);
    // 自动释放
}
```

#### 6.5.2 避免内存泄漏

```java
// 错误示例：未释放资源
public void wrongUsage() {
    for (int i = 0; i < 1000; i++) {
        FaceDetector detector = new FaceDetector(setting);  // 创建但未释放
        SeetaRect[] faces = detector.Detect(image);
    }
}

// 正确示例：使用对象池
public void correctUsage() {
    FaceDetectorProxy proxy = new FaceDetectorProxy(setting);
    for (int i = 0; i < 1000; i++) {
        SeetaRect[] faces = proxy.detect(image);  // 自动复用对象
    }
}
```

---

## 7. 常见问题

### 7.1 安装和配置问题

#### Q1: 提示 "UnsatisfiedLinkError" 或 "无法加载库"

**A:** 请检查以下几点：
- 确保系统已安装 Visual C++ Redistributable (Windows)
- 确保模型文件路径正确
- 确保使用正确的平台架构（32位/64位）

**解决方案：**

```bash
# Windows: 安装VC++运行库
# 下载地址: https://www.xitongzhijia.net/soft/234968.html

# 验证系统架构
java -version

# 检查模型路径
System.out.println(new File("model.csta").exists());
```

#### Q2: 在Docker中运行提示缺少 "libgomp.so" 依赖

**A:** 需要在Dockerfile中安装libgomp1库：

```dockerfile
FROM openjdk:8

RUN apt-get update && \
    apt-get install -y --no-install-recommends libgomp1 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /data/app
COPY target/seetaface6-sdk-*.jar /data/app/
CMD ["java", "-jar", "seetaface6-sdk-*.jar"]
```

#### Q3: GPU支持问题

**A:** 确保：
- 安装了正确的CUDA版本（11.6.2）
- GPU驱动版本匹配（512.15）
- 模型路径正确


### 7.2 使用问题

#### Q4: 检测不到人脸

**A:** 可能原因：
- 图像中人脸太小（小于设置的最小人脸尺寸）
- 图像质量差（模糊、光线暗）
- 人脸角度过大
- 检测阈值设置过高

**解决方案：**

```java
// 预处理图像
SeetaImageData processedImage = enhanceImage(image);
```

#### Q5: 相似度判断不准确

**A:** 建议：
- 针对不同场景使用不同的阈值
- 戴口罩场景使用 `face_recognizer_mask.csta`
- 定期更新模型和阈值

**动态阈值示例：**

```java
public class DynamicThreshold {
    public boolean isSamePerson(float similarity, String scenario) {
        switch (scenario) {
            case "security":
                return similarity > 0.70;
            case "attendance":
                return similarity > 0.65;
            case "unlock":
                return similarity > 0.60;
            default:
                return similarity > 0.62;
        }
    }
}
```

#### Q6: 性能问题

**A:** 优化建议：
- 使用对象池避免频繁创建/销毁对象
- 适当调整图像大小
- 使用GPU加速
- 调整线程数

**性能监控：**

```java
public class PerformanceMonitor {
    public static void measureTime(Runnable task, String name) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        double duration = (end - start) / 1_000_000.0;
        System.out.println(name + ": " + duration + " ms");
    }
}

// 使用示例
measureTime(() -> {
    SeetaRect[] faces = detector.detect(image);
}, "人脸检测");
```

### 7.3 Spring Boot集成问题

#### 参考资源
- **示例项目**: [Spring Boot集成示例](https://gitee.com/crazy-of-pig/spring-boot-seetaface6.git)

### 7.4 故障排除清单

- [ ] 检查JDK版本是否符合要求（8-14）
- [ ] 检查模型文件是否存在且可读
- [ ] 检查本地库是否正确加载
- [ ] 检查内存是否充足
- [ ] 检查日志文件中的错误信息
- [ ] 使用最简单的示例测试基本功能
- [ ] 确认对象池配置合理
- [ ] 检查并发场景下的线程安全

---

## 8. 技术支持

### 8.1 联系方式

- **QQ群**: 2284901326
- **微信**: jjs0684425
- **邮箱**: linyc1993@outlook.com

### 8.2 参考资源

- **官方文档**: [SeetaFace官网](http://www.seetatech.com/)
- **模型下载**: [百度网盘](https://pan.baidu.com/s/1LlXe2-YsUxQMe-MLzhQ2Aw)
- **示例项目**: [Spring Boot集成示例](https://gitee.com/crazy-of-pig/spring-boot-seetaface6.git)

### 8.3 更新日志

#### v2.0.0 (当前版本)
- ✅ 完善对象池管理
- ✅ 添加Proxy层简化API
- ✅ 支持ARM架构
- ✅ 修复国产x86机器AVX指令集问题
- ✅ 优化多线程性能
- ✅ 完善文档和示例

#### v1.0.0
- ✅ 基础SDK封装
- ✅ 支持人脸检测、识别、关键点
- ✅ 支持GPU加速

### 8.4 贡献指南

欢迎提交Issue和Pull Request！

1. Fork项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

### 8.5 开源协议

本项目采用 [MIT License](LICENSE) 协议开源。

---

<div align="center">

**如果这个项目对你有帮助，请给我们一个 ⭐ Star！**

</div>
