package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.QualityOfIntegrityPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.QualityOfIntegrity;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

public class QualityOfIntegrityProxy {

    private QualityOfIntegrityPool pool;

    public QualityOfIntegrityProxy() {

        pool = new QualityOfIntegrityPool(new SeetaConfSetting());
    }

    public QualityOfIntegrityProxy(SeetaConfSetting setting) {

        pool = new QualityOfIntegrityPool(setting);
    }

    public IntegrityItem check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks) {
        float[] score = new float[1];
        QualityOfIntegrity.QualityLevel qualityLevel = null;

        QualityOfIntegrity qualityOfIntegrity = null;

        try {
            qualityOfIntegrity = pool.borrowObject();
            qualityLevel = qualityOfIntegrity.check(imageData, face, landmarks, score);
        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (qualityOfIntegrity != null) {
                pool.returnObject(qualityOfIntegrity);
            }
        }

        return new IntegrityItem(qualityLevel, score[0]);

    }

    public class IntegrityItem {
        private QualityOfIntegrity.QualityLevel qualityLevel;

        private float score;

        public IntegrityItem(QualityOfIntegrity.QualityLevel qualityLevel, float score) {
            this.qualityLevel = qualityLevel;
            this.score = score;
        }

        public QualityOfIntegrity.QualityLevel getQualityLevel() {
            return qualityLevel;
        }

        public void setQualityLevel(QualityOfIntegrity.QualityLevel qualityLevel) {
            this.qualityLevel = qualityLevel;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }
    }
}
