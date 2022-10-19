package com.seeta.pool;

import com.seeta.sdk.FaceDetector;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;


/**
 *  人脸检测器连接池 检测到的每个人脸位置
 */
public class FaceDetectorPool extends GenericObjectPool<FaceDetector> {


    /**
     * this.factoryType = null;
     * this.maxIdle = 8;
     * this.minIdle = 0;
     * this.allObjects = new ConcurrentHashMap();
     * this.createCount = new AtomicLong(0L);
     * this.makeObjectCount = 0L;
     * this.makeObjectCountLock = new Object();
     * this.abandonedConfig = null;
     */
    public FaceDetectorPool(SeetaConfSetting config) {

        super(new PooledObjectFactory<FaceDetector>() {

            /**
             *  borrowObject方法的主要流程是首先看里面的idleReferences是否为空，如果不为空，则从里面取一个对象出来并返回，否则通过factory来创建一个object。
             * @return
             * @throws Exception
             */
            @Override
            public PooledObject makeObject() throws Exception {
                FaceDetector detector = new FaceDetector(config.getSeetaModelSetting());
                return new DefaultPooledObject(detector);
            }

            @Override
            public void destroyObject(PooledObject<FaceDetector> pooledObject)  throws Exception {
                FaceDetector object = pooledObject.getObject();
                object = null;
            }

            /**
             * 验证对象是否可用
             * @param pooledObject
             * @return
             */
            @Override
            public boolean validateObject(PooledObject<FaceDetector> pooledObject) {
                FaceDetector object = pooledObject.getObject();
                long impl = object.impl;
                return impl >= 0;
            }

            /**
             *  激活一个对象，使其可用用
             * @param pooledObject
             * @throws Exception
             */
            @Override
            public void activateObject(PooledObject<FaceDetector> pooledObject)  throws Exception {
                FaceDetector object = pooledObject.getObject();
                long impl = object.impl;
            }

            /**
             * 钝化一个对象,也可以理解为反初始化
             * @param pooledObject
             * @throws Exception
             */
            @Override
            public void passivateObject(PooledObject pooledObject)  throws Exception {
                //nothing
            }
        }, config);
    }

}
