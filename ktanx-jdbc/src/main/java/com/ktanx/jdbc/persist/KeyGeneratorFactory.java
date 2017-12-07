package com.ktanx.jdbc.persist;

/**
 * Created by liyd on 17/4/17.
 */
public interface KeyGeneratorFactory {


    /**
     * 执行器支持的类型
     *
     * @return
     */
    Class<?>[] getSupportType();

    /**
     * 获取执行器
     *
     * @return
     */
    KeyGenerator getKeyGenerator();
}
