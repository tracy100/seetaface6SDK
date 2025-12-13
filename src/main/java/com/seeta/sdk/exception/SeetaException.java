package com.seeta.sdk.exception;

/**
 * SeetaFace6 SDK基础异常类
 * 所有SDK相关异常的父类
 */
public class SeetaException extends RuntimeException {
    
    private final int errorCode;
    private final Object[] args;
    
    /**
     * 构造函数 - 仅消息
     */
    public SeetaException(String message) {
        super(message);
        this.errorCode = -1;
        this.args = null;
    }
    
    /**
     * 构造函数 - 消息和原因
     */
    public SeetaException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = -1;
        this.args = null;
    }
    
    /**
     * 构造函数 - 消息和错误码
     */
    public SeetaException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.args = null;
    }
    
    /**
     * 构造函数 - 完整信息
     */
    public SeetaException(String message, int errorCode, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }
    
    /**
     * 构造函数 - 完整信息with cause
     */
    public SeetaException(String message, Throwable cause, int errorCode, Object... args) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }
    
    /**
     * 获取错误码
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /**
     * 获取格式化参数
     */
    public Object[] getArgs() {
        return args;
    }
    
    /**
     * 获取格式化的详细消息
     */
    public String getFormattedMessage() {
        if (args != null && args.length > 0) {
            try {
                return String.format(super.getMessage(), args);
            } catch (Exception e) {
                return super.getMessage();
            }
        }
        return super.getMessage();
    }
    
    @Override
    public String toString() {
        String msg = getFormattedMessage();
        if (errorCode != -1) {
            return String.format("[Error %d] %s", errorCode, msg);
        }
        return msg;
    }
}
