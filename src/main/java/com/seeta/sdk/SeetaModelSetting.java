package com.seeta.sdk;

public class SeetaModelSetting {

    public SeetaDevice device;
    public int id;     // when device is GPU, id means GPU id
    public String[] model;

    public SeetaModelSetting(int id, String[] models, SeetaDevice dev) {
        this.id = id;
        this.device = dev;
        this.model = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            this.model[i] = models[i];
        }
    }


}
