package com.ktanx.jdbc.command;

import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/24.
 */
public abstract class AbstractCommandExecutorFactory implements CommandExecutorFactory {

    /**
     * 初始化执行器
     *
     * @param executor         the executor
     * @param clazz            the clazz
     * @param interfaceClass   the interface class
     * @param jdbcEngineConfig the jdbc engine config
     */
    protected void initCommandExecutor(AbstractCommandExecutor executor, Class<?> clazz, Class<?> interfaceClass, JdbcEngineConfig jdbcEngineConfig) {

        executor.setCommandContextBuilder(jdbcEngineConfig.getCommandContextBuilder(clazz, interfaceClass));
        executor.setDialect(jdbcEngineConfig.getDialect());
        executor.setType(clazz);
        executor.setPersistExecutor(jdbcEngineConfig.getPersistExecutor(clazz));

        executor.setMappingHandler(jdbcEngineConfig.getMappingHandler(clazz));
        executor.setPageHandler(jdbcEngineConfig.getPageHandler(clazz));
    }
}
