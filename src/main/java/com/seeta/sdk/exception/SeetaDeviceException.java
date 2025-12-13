package com.seeta.sdk.exception;

/**
 * 设备配置相关异常
 * 用于GPU/CPU设备配置、初始化等错误
 */
public class SeetaDeviceException extends SeetaException {
    
    public static final int DEVICE_NOT_AVAILABLE = 4001;
    public static final int GPU_NOT_FOUND = 4002;
    public static final int CUDA_INIT_FAILED = 4003;
    public static final int DEVICE_TYPE_INVALID = 4004;
    public static final int GPU_ID_INVALID = 4005;
    public static final int DEVICE_BUSY = 4006;
    
    public SeetaDeviceException(String message) {
        super(message);
    }
    
    public SeetaDeviceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SeetaDeviceException(String message, int errorCode) {
        super(message, errorCode);
    }
    
    public SeetaDeviceException(String message, int errorCode, Object... args) {
        super(message, errorCode, args);
    }
    
    public SeetaDeviceException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
    
    /**
     * 便捷方法：设备不可用
     */
    public static SeetaDeviceException deviceNotAvailable(String deviceType, int deviceId) {
        return new SeetaDeviceException(
            "%s设备 [%d] 不可用", 
            DEVICE_NOT_AVAILABLE, 
            deviceType, 
            deviceId
        );
    }
    
    /**
     * 便捷方法：未找到GPU
     */
    public static SeetaDeviceException gpuNotFound() {
        return new SeetaDeviceException(
            "未找到可用的GPU设备", 
            GPU_NOT_FOUND
        );
    }
    
    /**
     * 便捷方法：CUDA初始化失败
     */
    public static SeetaDeviceException cudaInitFailed(String reason) {
        return new SeetaDeviceException(
            "CUDA初始化失败: %s", 
            CUDA_INIT_FAILED, 
            reason
        );
    }
    
    /**
     * 便捷方法：设备类型无效
     */
    public static SeetaDeviceException deviceTypeInvalid(String deviceType) {
        return new SeetaDeviceException(
            "无效的设备类型: %s", 
            DEVICE_TYPE_INVALID, 
            deviceType
        );
    }
}
