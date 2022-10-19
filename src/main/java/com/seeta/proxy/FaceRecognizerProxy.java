package com.seeta.proxy;

import com.seeta.pool.FaceRecognizerPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.FaceRecognizer;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;


/**
 * 人脸特征提取评估器
 */
public class FaceRecognizerProxy {

    private FaceRecognizerPool pool;


    private FaceRecognizerProxy() {
    }


    public FaceRecognizerProxy(SeetaConfSetting config) {
        this.pool = new FaceRecognizerPool(config);
    }


    public float[] extract(SeetaImageData image, SeetaPointF[] points) {

        float[] features = null;
        FaceRecognizer faceRecognizer = null;

        try {
            faceRecognizer = pool.borrowObject();
            features = new float[faceRecognizer.GetExtractFeatureSize()];
            faceRecognizer.Extract(image, points, features);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (faceRecognizer != null) {
                pool.returnObject(faceRecognizer);
            }
        }

        return features;
    }

    public int getExtractFeatureSize() {

        FaceRecognizer faceRecognizer = null;

        try {
            faceRecognizer = pool.borrowObject();
            if (faceRecognizer != null) {
                return faceRecognizer.GetExtractFeatureSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (faceRecognizer != null) {
                pool.returnObject(faceRecognizer);
            }
        }

        return 0;
    }

    public float calculateSimilarity(float[] features1, float[] features2) {

        float score = -1f;
        FaceRecognizer faceRecognizer = null;

        try {
            faceRecognizer = pool.borrowObject();
            score = faceRecognizer.CalculateSimilarity(features1, features2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (faceRecognizer != null) {
                pool.returnObject(faceRecognizer);
            }
        }

        return score;
    }

    public float cosineSimilarity(float[] leftVector, float[] rightVector) {
        double dotProduct = 0;
        for (int i = 0; i < leftVector.length; i++) {
            dotProduct += leftVector[i] * rightVector[i];
        }
        double d1 = 0.0d;
        for (float value : leftVector) {
            d1 += Math.pow(value, 2);
        }
        double d2 = 0.0d;
        for (float value : rightVector) {
            d2 += Math.pow(value, 2);
        }
        double cosineSimilarity;
        if (d1 <= 0.0 || d2 <= 0.0) {
            cosineSimilarity = 0.0;
        } else {
            cosineSimilarity = (dotProduct / (Math.sqrt(d1) * Math.sqrt(d2)));
        }
        return (float) cosineSimilarity;
    }
}
