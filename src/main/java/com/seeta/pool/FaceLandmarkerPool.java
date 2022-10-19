package com.seeta.pool;

import com.seeta.sdk.FaceLandmarker;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;


/**
 * 人脸特征点检测器  有 5点和68点
 */
public class FaceLandmarkerPool extends GenericObjectPool<FaceLandmarker> {

    public FaceLandmarkerPool(SeetaConfSetting config) {
        super(new PooledObjectFactory<FaceLandmarker>() {

            @Override
            public PooledObject<FaceLandmarker> makeObject() throws Exception {
                FaceLandmarker faceLandmarker = new FaceLandmarker(config.getSeetaModelSetting());
                return new DefaultPooledObject<>(faceLandmarker);
            }

            @Override
            public void destroyObject(PooledObject<FaceLandmarker> pooledObject) throws Exception {
                FaceLandmarker object = pooledObject.getObject();

                object = null;
            }

            @Override
            public boolean validateObject(PooledObject<FaceLandmarker> pooledObject) {

                FaceLandmarker object = pooledObject.getObject();
                //这就不知道要写什么了
                return object.impl >= 0L;
            }

            @Override
            public void activateObject(PooledObject<FaceLandmarker> pooledObject) throws Exception {

            }

            @Override
            public void passivateObject(PooledObject<FaceLandmarker> pooledObject) throws Exception {

            }
        }, config);
    }


}
