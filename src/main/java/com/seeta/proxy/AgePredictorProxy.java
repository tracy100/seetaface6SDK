package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.AgePredictorPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.AgePredictor;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;


/**
 * 年龄评估器
 */
public class AgePredictorProxy {

    private AgePredictorPool pool;

    private AgePredictorProxy() {
    }


    public AgePredictorProxy(SeetaConfSetting config) {
        pool = new AgePredictorPool(config);
    }

    public int predictAgeWithCrop(SeetaImageData image, SeetaPointF[] points) {

        AgePredictor agePredictor = null;

        int[] age = new int[1];

        try {
            agePredictor = pool.borrowObject();
            agePredictor.PredictAgeWithCrop(image, points, age);
        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (agePredictor != null) {
                pool.returnObject(agePredictor);
            }
        }
        return age[0];
    }
}
