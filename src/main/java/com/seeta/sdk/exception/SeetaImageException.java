package com.seeta.sdk.exception;

/**
 * 图像处理相关异常
 * 用于图像加载、转换、处理等错误
 */
public class SeetaImageException extends SeetaException {
    
    public static final int IMAGE_LOAD_FAILED = 2001;
    public static final int IMAGE_FORMAT_INVALID = 2002;
    public static final int IMAGE_TOO_LARGE = 2003;
    public static final int IMAGE_TOO_SMALL = 2004;
    public static final int IMAGE_EMPTY = 2005;
    public static final int IMAGE_PROCESSING_FAILED = 2006;
    public static final int IMAGE_NULL = 2007;
    
    public SeetaImageException(String message) {
        super(message);
    }
    
    public SeetaImageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SeetaImageException(String message, int errorCode) {
        super(message, errorCode);
    }
    
    public SeetaImageException(String message, int errorCode, Object... args) {
        super(message, errorCode, args);
    }
    
    public SeetaImageException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
    
    /**
     * 便捷方法：图像加载失败
     */
    public static SeetaImageException imageLoadFailed(String imagePath, Throwable cause) {
        return new SeetaImageException(
            "图像加载失败: %s", 
            cause, 
            IMAGE_LOAD_FAILED, 
            imagePath
        );
    }
    
    /**
     * 便捷方法：图像为空
     */
    public static SeetaImageException imageNull() {
        return new SeetaImageException(
            "图像数据不能为空", 
            IMAGE_NULL
        );
    }
    
    /**
     * 便捷方法：图像格式无效
     */
    public static SeetaImageException imageFormatInvalid(String format) {
        return new SeetaImageException(
            "不支持的图像格式: %s", 
            IMAGE_FORMAT_INVALID, 
            format
        );
    }
    
    /**
     * 便捷方法：图像处理失败
     */
    public static SeetaImageException imageProcessingFailed(String operation, Throwable cause) {
        return new SeetaImageException(
            "图像处理失败 [%s]", 
            cause, 
            IMAGE_PROCESSING_FAILED, 
            operation
        );
    }
}
