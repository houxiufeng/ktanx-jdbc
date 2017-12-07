package com.ktanx.jdbc.command;

import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * 执行器工厂
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface CommandExecutorFactory {


    /**
     * 执行器支持的类型
     *
     * @return
     */
    Class<?>[] getSupportType();

    /**
     * 执行器本身实现在类型
     *
     * @return
     */
    Class<? extends CommandExecutor> getExecutorInterface();

    /**
     * 获取执行器
     *
     * @param clazz            the clazz
     * @param jdbcEngineConfig the jdbc engine config
     * @return executor
     */
    <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig);

}
