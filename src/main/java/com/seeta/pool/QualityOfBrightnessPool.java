package com.seeta.pool;

import com.seeta.sdk.QualityOfBrightness;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class QualityOfBrightnessPool extends GenericObjectPool<QualityOfBrightness> {

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
    public QualityOfBrightnessPool(SeetaConfSetting config) {

        super(new PooledObjectFactory<QualityOfBrightness>() {

            /**
             *  borrowObject方法的主要流程是首先看里面的idleReferences是否为空，如果不为空，则从里面取一个对象出来并返回，否则通过factory来创建一个object。
             * @return
             * @throws Exception
             */
            @Override
            public PooledObject makeObject() throws Exception {
                QualityOfBrightness detector = new QualityOfBrightness();
                return new DefaultPooledObject(detector);
            }

            @Override
            public void destroyObject(PooledObject<QualityOfBrightness> pooledObject) throws Exception {
                QualityOfBrightness object = pooledObject.getObject();
                object = null;
            }

            /**
             * 验证对象是否可用
             * @param pooledObject
             * @return
             */
            @Override
            public boolean validateObject(PooledObject<QualityOfBrightness> pooledObject) {
                QualityOfBrightness object = pooledObject.getObject();
                long impl = object.impl;
                return impl >= 0;
            }

            /**
             *  激活一个对象，使其可用用
             * @param pooledObject
             * @throws Exception
             */
            @Override
            public void activateObject(PooledObject<QualityOfBrightness> pooledObject) throws Exception {
                QualityOfBrightness object = pooledObject.getObject();
                long impl = object.impl;
            }

            /**
             * 钝化一个对象,也可以理解为反初始化
             * @param pooledObject
             * @throws Exception
             */
            @Override
            public void passivateObject(PooledObject pooledObject) throws Exception {
                //nothing
            }
        }, config);
    }
}
