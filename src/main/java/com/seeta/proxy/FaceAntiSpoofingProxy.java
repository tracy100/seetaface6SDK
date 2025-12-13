package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.FaceAntiSpoofingPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.FaceAntiSpoofing;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

public class FaceAntiSpoofingProxy {

    private FaceAntiSpoofingPool pool;

    private FaceAntiSpoofingProxy() {
    }

    public FaceAntiSpoofingProxy(SeetaConfSetting setting) {
        pool = new FaceAntiSpoofingPool(setting);
    }

    public FaceAntiSpoofing.Status predict(SeetaImageData image, SeetaRect face, SeetaPointF[] landmarks) {

        FaceAntiSpoofing.Status status = null;
        FaceAntiSpoofing faceAntiSpoofing = null;
        try {

            faceAntiSpoofing = pool.borrowObject();
            status = faceAntiSpoofing.Predict(image, face, landmarks);

        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (faceAntiSpoofing != null) {
                pool.returnObject(faceAntiSpoofing);
            }
        }

        return status;
    }
}
