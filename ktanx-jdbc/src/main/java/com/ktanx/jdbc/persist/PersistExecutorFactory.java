package com.ktanx.jdbc.persist;

/**
 * Created by liyd on 17/4/12.
 */
public interface PersistExecutorFactory {

    /**
     * 支持类型
     *
     * @return
     */
    Class<?>[] getSupportType();

    /**
     * 获取执行器
     *
     * @return
     */
    PersistExecutor getExecutor();
}
