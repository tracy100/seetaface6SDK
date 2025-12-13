package com.seeta.proxy;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.exception.SeetaException;
import com.seeta.pool.QualityOfPoseExPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.QualityOfPoseEx;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

public class QualityOfPoseExProxy {

    private QualityOfPoseExPool pool;

    private QualityOfPoseExProxy() {
    }


    public QualityOfPoseExProxy(SeetaConfSetting setting) {

        pool = new QualityOfPoseExPool(setting);

    }

    /**
     * 检测人脸姿态
     *
     * @param imageData
     * @param face
     * @param landmarks
     * @return
     */
    public QualityOfPoseEx.QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks) {

        QualityOfPoseEx.QualityLevel qualityLevel = null;
        QualityOfPoseEx qualityOfPoseEx = null;

        float[] scors = new float[1];
        try {
            qualityOfPoseEx = pool.borrowObject();
            qualityLevel = qualityOfPoseEx.check(imageData, face, landmarks, scors);

        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (qualityOfPoseEx != null) {

                pool.returnObject(qualityOfPoseEx);
            }
        }
        return qualityLevel;
    }

    /**
     * 检测人脸姿态
     *
     * @param imageData [input]image data
     * @param face      [input] face location
     * @param landmarks [input] face landmarks
     * @return yaw       [output] face location in yaw  偏航中的面部位置
     * @return pitch     [output] face location in pitch 俯仰中的面部位置
     * @return roll      [oputput] face location in roll  面卷中的位置
     */
    public PoseExItem checkCore(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks) {
        float[] yaw = new float[1];
        float[] pitch = new float[1];
        float[] roll = new float[1];

        QualityOfPoseEx qualityOfPoseEx = null;

        try {
            qualityOfPoseEx = pool.borrowObject();

            qualityOfPoseEx.check(imageData, face, landmarks, yaw, pitch, roll);
        } catch (Exception e) {
            throw new SeetaException("操作失败: " + e.getMessage(), e);
        } finally {
            if (qualityOfPoseEx != null) {
                pool.returnObject(qualityOfPoseEx);
            }
        }

        return new PoseExItem(yaw[0], pitch[0], roll[0]);

    }

    public class PoseExItem {

        private float yaw;
        private float pitch;
        private float roll;

        public PoseExItem(float yaw, float pitch, float roll) {
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
