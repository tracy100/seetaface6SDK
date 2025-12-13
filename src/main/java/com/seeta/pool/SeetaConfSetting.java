package com.seeta.pool;

import com.seeta.sdk.SeetaDevice;
import com.seeta.sdk.SeetaModelSetting;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.FileNotFoundException;


/**
 * 用于构建常用的配置
 * maxTotal 对象总数 8
 * maxIdle  最大空闲对象数 8
 * minIdle  最小空闲对象书 0
 * lifom    对象池借还是否采用 lifo true
 * fairness 对于借对象的线程阻塞恢复公平性 false
 * maxWaitMillis 借对象阻塞最大等待时间 -1
 * minEvictableIdleTimeMillis  最小驱逐空闲时间 30分钟
 * numTestsPerEvictionRun  每次驱逐数量 3
 * testOnCreate  创建后有效性测试  false
 * testOnBorrow  出借前有效性测试  false
 * testOnReturn  还回前有效性测试  false
 * testWhileIdle 空闲有效性测试  false
 * timeBetweenEvictionRunsMillis 驱逐定时器周期  false
 * blockWhenExhausted 对象池耗尽是否 block true
 */
public class SeetaConfSetting extends GenericObjectPoolConfig {

    /**
     * 评估器用的配置文件
     */
    private SeetaModelSetting seetaModelSetting;

    public SeetaConfSetting() {
    }

    public SeetaConfSetting(SeetaModelSetting seetaModelSetting) {
        this.seetaModelSetting = seetaModelSetting;
    }

    public SeetaConfSetting(int id, String[] models, SeetaDevice dev) throws FileNotFoundException {
        this.seetaModelSetting = new SeetaModelSetting(id, models, dev);
    }

    public SeetaConfSetting(String[] models) throws FileNotFoundException {
        this.seetaModelSetting = new SeetaModelSetting(0, models, SeetaDevice.SEETA_DEVICE_AUTO);
    }

    public SeetaConfSetting(String model) throws FileNotFoundException {
        this.seetaModelSetting = new SeetaModelSetting(0, new String[]{model}, SeetaDevice.SEETA_DEVICE_AUTO);
    }


    public SeetaModelSetting getSeetaModelSetting() {
        return seetaModelSetting;
    }

    public void setSeetaModelSetting(SeetaModelSetting seetaModelSetting) {
        this.seetaModelSetting = seetaModelSetting;
    }
}
