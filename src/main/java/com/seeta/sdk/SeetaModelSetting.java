package com.seeta.sdk;

import java.io.File;
import java.io.FileNotFoundException;

public class SeetaModelSetting {

    public SeetaDevice device;
    public int id;     // when device is GPU, id means GPU id
    public String[] model;

    public SeetaModelSetting(int id, String[] models, SeetaDevice dev) throws FileNotFoundException {
        this.id = id;
        this.device = dev;
        this.model = new String[models.length];

        for (int i = 0; i < models.length; i++) {
            //添加验证
            File file = new File(models[i]);
            if (!file.exists()) {
               throw  new FileNotFoundException("模型文件没有找到！");
            }
            this.model[i] = models[i];

        }
    }


    public SeetaModelSetting(String[] models, SeetaDevice dev) throws FileNotFoundException {
        this.id = 0;
        this.device = dev;
        this.model = new String[models.length];

        for (int i = 0; i < models.length; i++) {
            //添加验证
            File file = new File(models[i]);
            if (!file.exists()) {
                throw  new FileNotFoundException("模型文件没有找到！");
            }
            this.model[i] = models[i];

        }
    }

}
