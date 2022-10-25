package com.seeta.proxy;

import com.seeta.pool.FaceLandmarkerPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.FaceLandmarker;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;
import javafx.util.Pair;


/**
 * 人脸特征点检测器  有 5点和68点
 */
public class FaceLandmarkerProxy {

    private FaceLandmarkerPool pool;

    private FaceLandmarkerProxy() {
    }

    public FaceLandmarkerProxy(SeetaConfSetting config) {
        pool = new FaceLandmarkerPool(config);
    }

    public SeetaPointF[] mark(SeetaImageData imageData, SeetaRect seetaRect) {

        FaceLandmarker faceLandmarker = null;
        SeetaPointF[] pointFS = null;

        try {

            faceLandmarker = pool.borrowObject();
            pointFS = new SeetaPointF[faceLandmarker.number()];
            faceLandmarker.mark(imageData, seetaRect, pointFS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (faceLandmarker != null) {
                pool.returnObject(faceLandmarker);
            }
        }

        return pointFS;
    }

    public Pair<SeetaPointF[], int[]> isMask(SeetaImageData imageData, SeetaRect seetaRect) {
        FaceLandmarker faceLandmarker = null;
        SeetaPointF[] pointFS = null;
        int[] masks =  null;
        try {
            faceLandmarker = pool.borrowObject();
            pointFS = new SeetaPointF[faceLandmarker.number()];
            masks = new int[faceLandmarker.number()];
            faceLandmarker.mark(imageData, seetaRect, pointFS, masks);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (faceLandmarker != null) {
                pool.returnObject(faceLandmarker);
            }
        }
        return new Pair<>(pointFS, masks);
    }

    public int number() {
        FaceLandmarker faceLandmarker = null;
        try {
            faceLandmarker = pool.borrowObject();
            if (faceLandmarker != null) {
                return faceLandmarker.number();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (faceLandmarker != null) {
                pool.returnObject(faceLandmarker);
            }
        }
        return 0;
    }


}
