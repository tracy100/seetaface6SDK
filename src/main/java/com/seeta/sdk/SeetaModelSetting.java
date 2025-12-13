package com.seeta.sdk;

import com.seeta.sdk.exception.SeetaModelException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * SeetaFace6模型配置类
 */
public class SeetaModelSetting {

    public SeetaDevice device;
    public int id;     // when device is GPU, id means GPU id
    public String[] model;

    /**
     * 构造函数 - 支持从JAR包加载模型文件
     * @param id 设备ID (GPU ID when using GPU)
     * @param models 模型文件路径数组
     * @param dev 设备类型 (CPU/GPU/AUTO)
     * @throws SeetaModelException 模型相关异常
     */
    public SeetaModelSetting(int id, String[] models, SeetaDevice dev) throws SeetaModelException {
        this.id = id;
        this.device = dev;
        this.model = new String[models.length];

        for (int i = 0; i < models.length; i++) {
            if (models[i] == null || models[i].trim().isEmpty()) {
                throw SeetaModelException.modelFormatError("模型路径", "路径不能为空");
            }
            this.model[i] = models[i];
        }
    }

    /**
     * 构造函数 - 简化版本
     * @param models 模型文件路径数组
     * @param dev 设备类型
     * @throws SeetaModelException 模型相关异常
     */
    public SeetaModelSetting(String[] models, SeetaDevice dev) throws SeetaModelException {
        this(0, models, dev);
    }
}
