package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.QualityOfResolutionPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.QualityOfResolution;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

public class QualityOfResolutionProxy {

    private QualityOfResolutionPool pool;


    public QualityOfResolutionProxy() {

        pool = new QualityOfResolutionPool(new SeetaConfSetting());
    }

    public QualityOfResolutionProxy(SeetaConfSetting confSetting) {

        pool = new QualityOfResolutionPool(confSetting);
    }


    /**
     * 评估人脸尺寸
     *
     * @param imageData
     * @param face
     * @param landmarks [output] quality score
     * @return QualityLevel
     */
    public QualityOfResolution.QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks) {

        float[] score = new float[1];

        QualityOfResolution.QualityLevel qualityLevel = null;
        QualityOfResolution qualityOfResolution = null;
        try {
            qualityOfResolution = pool.borrowObject();
            qualityLevel = qualityOfResolution.check(imageData, face, landmarks, score);
        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (qualityOfResolution != null) {
                pool.returnObject(qualityOfResolution);
            }
        }
        return qualityLevel;
    }

}
