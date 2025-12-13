package com.seeta.sdk.exception;

/**
 * SDK初始化相关异常
 * 用于SDK初始化、库加载等错误
 */
public class SeetaInitializationException extends SeetaException {
    
    public static final int NATIVE_LIBRARY_LOAD_FAILED = 5001;
    public static final int SDK_INIT_FAILED = 5002;
    public static final int AUTHORIZATION_FAILED = 5003;
    public static final int DEPENDENCY_MISSING = 5004;
    public static final int VERSION_MISMATCH = 5005;
    
    public SeetaInitializationException(String message) {
        super(message);
    }
    
    public SeetaInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SeetaInitializationException(String message, int errorCode) {
        super(message, errorCode);
    }
    
    public SeetaInitializationException(String message, int errorCode, Object... args) {
        super(message, errorCode, args);
    }
    
    public SeetaInitializationException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
    
    /**
     * 便捷方法：本地库加载失败
     */
    public static SeetaInitializationException nativeLibraryLoadFailed(String libraryName, Throwable cause) {
        return new SeetaInitializationException(
            "本地库加载失败: %s", 
            cause, 
            NATIVE_LIBRARY_LOAD_FAILED, 
            libraryName
        );
    }
    
    /**
     * 便捷方法：SDK初始化失败
     */
    public static SeetaInitializationException sdkInitFailed(String component, Throwable cause) {
        return new SeetaInitializationException(
            "SDK组件初始化失败: %s", 
            cause, 
            SDK_INIT_FAILED, 
            component
        );
    }
    
    /**
     * 便捷方法：授权失败
     */
    public static SeetaInitializationException authorizationFailed(String reason) {
        return new SeetaInitializationException(
            "SDK授权失败: %s", 
            AUTHORIZATION_FAILED, 
            reason
        );
    }
    
    /**
     * 便捷方法：依赖缺失
     */
    public static SeetaInitializationException dependencyMissing(String dependency) {
        return new SeetaInitializationException(
            "缺少必要的依赖: %s", 
            DEPENDENCY_MISSING, 
            dependency
        );
    }
}
