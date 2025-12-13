package com.seeta.sdk.exception;

/**
 * 模型相关异常
 * 用于模型文件加载、初始化、配置等错误
 */
public class SeetaModelException extends SeetaException {
    
    public static final int MODEL_NOT_FOUND = 1001;
    public static final int MODEL_LOAD_FAILED = 1002;
    public static final int MODEL_INVALID = 1003;
    public static final int MODEL_VERSION_MISMATCH = 1004;
    public static final int MODEL_FORMAT_ERROR = 1005;
    public static final int MODEL_PATH_INVALID = 1006;
    
    public SeetaModelException(String message) {
        super(message);
    }
    
    public SeetaModelException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SeetaModelException(String message, int errorCode) {
        super(message, errorCode);
    }
    
    public SeetaModelException(String message, int errorCode, Object... args) {
        super(message, errorCode, args);
    }
    
    public SeetaModelException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
    
    /**
     * 便捷方法：模型文件未找到
     */
    public static SeetaModelException modelNotFound(String modelPath) {
        return new SeetaModelException(
            "模型文件未找到: %s", 
            MODEL_NOT_FOUND, 
            modelPath
        );
    }
    
    /**
     * 便捷方法：模型加载失败
     */
    public static SeetaModelException modelLoadFailed(String modelPath, Throwable cause) {
        return new SeetaModelException(
            "模型加载失败: %s", 
            cause, 
            MODEL_LOAD_FAILED, 
            modelPath
        );
    }
    
    /**
     * 便捷方法：模型格式错误
     */
    public static SeetaModelException modelFormatError(String modelPath, String reason) {
        return new SeetaModelException(
            "模型格式错误 [%s]: %s", 
            MODEL_FORMAT_ERROR, 
            modelPath, 
            reason
        );
    }
}
