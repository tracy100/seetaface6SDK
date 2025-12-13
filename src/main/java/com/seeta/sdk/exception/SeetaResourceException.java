package com.seeta.sdk.exception;

/**
 * 资源管理相关异常
 * 用于内存、对象池、设备资源等错误
 */
public class SeetaResourceException extends SeetaException {
    
    public static final int OUT_OF_MEMORY = 3001;
    public static final int POOL_EXHAUSTED = 3002;
    public static final int RESOURCE_LEAK = 3003;
    public static final int DISPOSE_FAILED = 3004;
    public static final int GPU_MEMORY_EXHAUSTED = 3005;
    public static final int RESOURCE_INITIALIZATION_FAILED = 3006;
    
    public SeetaResourceException(String message) {
        super(message);
    }
    
    public SeetaResourceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SeetaResourceException(String message, int errorCode) {
        super(message, errorCode);
    }
    
    public SeetaResourceException(String message, int errorCode, Object... args) {
        super(message, errorCode, args);
    }
    
    public SeetaResourceException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
    
    /**
     * 便捷方法：GPU内存不足
     */
    public static SeetaResourceException gpuMemoryExhausted(long required, long available) {
        return new SeetaResourceException(
            "GPU内存不足 - 需要: %d MB, 可用: %d MB", 
            GPU_MEMORY_EXHAUSTED, 
            required, 
            available
        );
    }
    
    /**
     * 便捷方法：对象池耗尽
     */
    public static SeetaResourceException poolExhausted(String poolName, int maxSize) {
        return new SeetaResourceException(
            "对象池 [%s] 已耗尽 (最大: %d)", 
            POOL_EXHAUSTED, 
            poolName, 
            maxSize
        );
    }
    
    /**
     * 便捷方法：资源释放失败
     */
    public static SeetaResourceException disposeFailed(String resourceType, Throwable cause) {
        return new SeetaResourceException(
            "%s 资源释放失败", 
            cause, 
            DISPOSE_FAILED, 
            resourceType
        );
    }
    
    /**
     * 便捷方法：资源初始化失败
     */
    public static SeetaResourceException resourceInitializationFailed(String resourceType, Throwable cause) {
        return new SeetaResourceException(
            "%s 资源初始化失败", 
            cause, 
            RESOURCE_INITIALIZATION_FAILED, 
            resourceType
        );
    }
}
