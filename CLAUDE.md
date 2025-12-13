# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

This is a Maven-based Java project wrapping SeetaFace6 C++ SDK with JNI.

**Build the project:**

```bash
mvn clean package
```

**Run a specific test:**

```bash
mvn test -Dtest=DetectorTest
# or for other tests:
mvn test -Dtest=FaceRecognizerTest
mvn test -Dtest=LandmarkerTest
mvn test -Dtest=AntiSpoofingTest
```

**Run all tests:**

```bash
mvn test
```

**Install to local Maven repository:**

```bash
mvn install
```

## Code Architecture

### Three-Tier Architecture Pattern

The SDK uses a layered architecture with **SDK → Proxy → Pool** pattern:

1. **SDK Layer** (`src/main/java/com/seeta/sdk/`): Core Java classes with native JNI methods
    - Direct wrappers around C++ functionality
    - Classes: `FaceDetector`, `FaceRecognizer`, `FaceLandmarker`, `AgePredictor`, etc.
    - Each class has a `long impl` field holding native pointer
    - Native methods defined but implemented in C++ JNI libraries

2. **Proxy Layer** (`src/main/java/com/seeta/proxy/`): Simplified API layer
    - Wraps Pool functionality for easier usage
    - Classes: `FaceDetectorProxy`, `FaceRecognizerProxy`, etc.
    - Provides cleaner `detect()`, `recognize()` methods
    - Example: `FaceDetectorProxy` calls `FaceDetectorPool.borrowObject()` and automatically returns it

3. **Pool Layer** (`src/main/java/com/seeta/pool/`): Object pooling with Apache Commons Pool2
    - Manages lifecycle of native SDK objects
    - Classes: `FaceDetectorPool`, `FaceRecognizerPool`, etc.
    - Configured via `SeetaConfSetting`
    - Improves performance by reusing expensive native objects

### Native Library Loading

**Loading mechanism** (`src/main/java/com/seeta/sdk/util/LoadNativeCore.java`):

- Automatically loads platform-specific native libraries from `src/main/resources/seetaface6/`
- Architecture detection: amd64, aarch64, arm
- OS detection: Windows, Linux
- GPU/CPU detection via `SeetaDevice` enum
- Loads libraries in dependency order specified by `dll.properties`
- Copies libraries from JAR to temp directory and loads them with `System.load()`

**Library organization** (per platform in resources):

```
seetaface6/
├── windows/amd64/
│   ├── dll.properties          # Load order configuration
│   ├── base/                   # Core Seeta libraries
│   │   ├── SeetaAuthorize.dll
│   │   ├── tennis*.dll         # SIMD optimizations
│   │   ├── SeetaFaceDetector600.dll
│   │   └── ...
│   └── *.dll                   # JNI wrapper libraries
├── linux/amd64/
├── linux/aarch64/
└── linux/arm/
```

**Device configuration** (`src/main/java/com/seeta/sdk/SeetaDevice.java`):

- `SEETA_DEVICE_AUTO`: Auto-detect (default)
- `SEETA_DEVICE_CPU`: Force CPU
- `SEETA_DEVICE_GPU`: Force GPU (requires CUDA)

### Key Classes

**Model Configuration** (`src/main/java/com/seeta/sdk/SeetaModelSetting.java`):

- Holds model file paths and device configuration
- Used to initialize all SDK components

**Image Data** (`src/main/java/com/seeta/sdk/SeetaImageData.java`):

- Wraps image data for native calls
- Contains width, height, channels, and pixel data

**Test Constants** (`src/test/java/com/seeta/sdk/FileConstant.java`):

- Windows-specific paths to model files and test images
- Modify these paths for your environment

## Testing

**Test structure:**

- `src/test/java/com/seeta/sdk/base/`: Core functionality tests
- `src/test/java/com/seeta/sdk/proxy/`: Proxy layer tests
- `src/test/java/com/seeta/sdk/thread/`: Thread safety tests
- `src/test/java/com/seeta/sdk/memory/`: Memory leak tests
- `img/`: Test images (qq.jpg, 攻击人脸检测.jpg)

**Running tests:**

```bash
# Test with Proxy layer (recommended for production)
mvn test -Dtest=*ProxyTest

# Test core SDK directly
mvn test -Dtest=*Test -Dtest.exclude=*ProxyTest

# Test memory management
mvn test -Dtest=MemoryTest
```

**Test initialization:**
All tests call `LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO)` in a static block to load native libraries.

## Model Files

**Required .csta model files** (download from README links):

- `face_detector.csta` - Face detection
- `face_recognizer.csta` - Face recognition (1024-dim features)
- `face_recognizer_mask.csta` - Masked face recognition (512-dim)
- `face_recognizer_light.csta` - Lightweight recognition (512-dim)
- `face_landmarker_pts5.csta` - 5-point landmarks
- `face_landmarker_pts68.csta` - 68-point landmarks
- `age_predictor.csta` - Age estimation
- `gender_predictor.csta` - Gender classification
- `eye_state.csta` - Eye state detection
- `mask_detector.csta` - Mask detection
- `pose_estimation.csta` - Pose estimation
- `quality_lbn.csta` - Image quality assessment
- `fas_first.csta`, `fas_second.csta` - Anti-spoofing models

**Configuration:**
Update paths in `src/test/java/com/seeta/sdk/FileConstant.java`:

```java
public static final String CSTA_PATH = "D:\\face\\models";
public static final String TEST_PICT = "D:\\face\\image\\me\\00.jpg";
```

## Dependencies

**Maven dependencies:**

- `commons-pool2:2.5.0` - Object pooling

**Native dependencies:**

- Pre-compiled SeetaFace6 libraries (included in resources)
- CUDA 11.6.2 + driver 512.15 (for GPU support)
- Visual C++ Redistributable (Windows)

## Platform Support

- **OS**: Windows 7/10/11, Linux
- **Arch**: amd64 (x86_64), aarch64, arm
- **Java**: JDK 8-14
- **GPU**: CUDA-enabled GPUs (optional)

## Common Development Tasks

**Adding a new SDK component:**

1. Create Java class in `com.seeta.sdk` with native methods
2. Implement C++ JNI wrapper
3. Create `*Proxy` class in `com.seeta.proxy`
4. Create `*Pool` class in `com.seeta.pool`
5. Add test in `src/test/java/com/seeta/sdk/base/`
6. Update model paths in `FileConstant.java`
7. Add library entries to `dll.properties`

**GPU support:**

- Set device in `SeetaModelSetting`: `new SeetaModelSetting(paths, SeetaDevice.SEETA_DEVICE_GPU)`
- Requires CUDA installation matching compiled version

**Spring Boot integration:**
See README.md section 4 for bean configuration example using `*Proxy` classes with object pooling.

## Important Notes

- **Static initialization**: Always call `LoadNativeCore.LOAD_NATIVE()` before using any SDK class
- **Resource cleanup**: Pool pattern automatically handles native object disposal
- **Thread safety**: Proxy layer is thread-safe when using pool
- **Model files**: Must be downloaded separately and paths configured
- **Native libraries**: Automatically extracted to temp directory at runtime
