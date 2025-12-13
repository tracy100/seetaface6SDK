package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.EyeStateDetectorPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.EyeStateDetector;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;

public class EyeStateDetectorProxy {

    private EyeStateDetectorPool pool;

    private EyeStateDetectorProxy() {
    }

    public EyeStateDetectorProxy(SeetaConfSetting confSetting) {
        pool = new EyeStateDetectorPool(confSetting);
    }

    public EyeStateDetector.EYE_STATE[] detect(SeetaImageData imageData, SeetaPointF[] points) {

        EyeStateDetector eyeStateDetector = null;
        EyeStateDetector.EYE_STATE[] states = null;

        try {

            eyeStateDetector = pool.borrowObject();
            states = eyeStateDetector.detect(imageData, points);

        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (eyeStateDetector != null) {
                pool.returnObject(eyeStateDetector);
            }
        }

        return states;
    }
}
