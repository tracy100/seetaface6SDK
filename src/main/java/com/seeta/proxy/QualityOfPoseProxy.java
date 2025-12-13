package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.QualityOfPosePool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.QualityOfPose;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

public class QualityOfPoseProxy {

    private QualityOfPosePool pool;

    public QualityOfPoseProxy() {
        pool = new QualityOfPosePool(new SeetaConfSetting());
    }

    public QualityOfPoseProxy(SeetaConfSetting confSetting) {
        pool = new QualityOfPosePool(confSetting);
    }

    public QualityOfPose.QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks) {

        float[] score = new float[1];
        QualityOfPose qualityOfPose = null;
        QualityOfPose.QualityLevel check = null;
        try {
            qualityOfPose = pool.borrowObject();
            check = qualityOfPose.check(imageData, face, landmarks, score);

        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (qualityOfPose != null) {
                pool.returnObject(qualityOfPose);
            }
        }
        return check;
    }


}
