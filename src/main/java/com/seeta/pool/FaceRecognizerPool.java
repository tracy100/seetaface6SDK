package com.seeta.pool;

import com.seeta.sdk.exception.SeetaResourceException;
import com.seeta.sdk.FaceRecognizer;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;


/**
 * 人脸向量特征识别器
 */
public class FaceRecognizerPool extends GenericObjectPool<FaceRecognizer> {


    public FaceRecognizerPool(SeetaConfSetting config) {
        super(new PooledObjectFactory<FaceRecognizer>() {

            @Override
            public PooledObject<FaceRecognizer> makeObject() throws Exception {

                return new DefaultPooledObject<>(new FaceRecognizer(config.getSeetaModelSetting()));
            }

            @Override
            public void destroyObject(PooledObject<FaceRecognizer> pooledObject) throws SeetaResourceException {

                FaceRecognizer object = pooledObject.getObject();
                if (object != null) {
                    object.dispose();
                }

            }

            @Override
            public boolean validateObject(PooledObject<FaceRecognizer> pooledObject) {

                FaceRecognizer object = pooledObject.getObject();
                return object.impl >= 0L;
            }

            @Override
            public void activateObject(PooledObject<FaceRecognizer> pooledObject) throws Exception {

            }

            @Override
            public void passivateObject(PooledObject<FaceRecognizer> pooledObject) throws Exception {

            }
        }, config);
    }


}
