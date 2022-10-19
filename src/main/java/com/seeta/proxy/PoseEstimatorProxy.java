package com.seeta.proxy;

import com.seeta.pool.PoseEstimatorPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.PoseEstimator;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaRect;

public class PoseEstimatorProxy {

    private PoseEstimatorPool pool;

    public PoseEstimatorProxy(SeetaConfSetting confSetting) {

        pool = new PoseEstimatorPool(confSetting);
    }

    public PoseItem estimate(SeetaImageData image, SeetaRect face) {
        float[] yaw = new float[1];
        float[] pitch = new float[1];
        float[] roll = new float[1];

        PoseEstimator poseEstimator = null;

        try {
            poseEstimator = pool.borrowObject();
            poseEstimator.Estimate(image, face, yaw, pitch, roll);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (poseEstimator != null) {
                pool.returnObject(poseEstimator);
            }
        }
        return new PoseItem(yaw[0], pitch[0], roll[0]);
    }


    public class PoseItem {
        private float yaw;
        private float pitch;
        private float roll;

        public PoseItem(float yaw, float pitch, float roll) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getRoll() {
            return roll;
        }

        public void setRoll(float roll) {
            this.roll = roll;
        }
    }

}
