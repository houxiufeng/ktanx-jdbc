package com.ktanx.jdbc.config;

import com.ktanx.jdbc.command.CommandExecutor;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcEngine {


    /**
     * 创建执行器
     *
     * @param executorClass 执行器class
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T createExecutor(Class<T> executorClass);

    /**
     * 创建执行器
     *
     * @param entityClass   实体class
     * @param executorClass 执行器class
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T createExecutor(Class<?> entityClass, Class<T> executorClass);

}
