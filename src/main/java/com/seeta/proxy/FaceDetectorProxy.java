package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.FaceDetectorPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.FaceDetector;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaRect;

/**
 * 人脸位置评估器
 */
public class FaceDetectorProxy {

    private FaceDetectorPool faceDetectorPool;

    private FaceDetectorProxy() {
    }

    public FaceDetectorProxy(SeetaConfSetting config) {
        faceDetectorPool = new FaceDetectorPool(config);
    }


    public SeetaRect[] detect(SeetaImageData image) throws Exception {
        FaceDetector faceDetector = null;

        SeetaRect[] detect;

        try {
            faceDetector = faceDetectorPool.borrowObject();
            detect = faceDetector.Detect(image);
        } finally {
            if (faceDetector != null) {
                faceDetectorPool.returnObject(faceDetector);
            }
        }

        return detect;
    }


//    public void setProperty(FaceDetector.Property property, double value) throws Exception {
//
//        FaceDetector faceDetector = null;
//        SeetaRect[] detect;
//        try {
//            faceDetector = faceDetectorPool.borrowObject();
//
//        }finally {
//            if (faceDetector != null) {
//                faceDetectorPool.returnObject(faceDetector);
//            }
//        }
//
//    }

}
