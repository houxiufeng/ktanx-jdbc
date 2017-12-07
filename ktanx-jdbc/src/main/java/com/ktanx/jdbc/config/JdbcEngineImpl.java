package com.ktanx.jdbc.config;

import com.ktanx.jdbc.command.CommandExecutor;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcEngineImpl implements JdbcEngine {

    private JdbcEngineConfig jdbcEngineConfig;

    public JdbcEngineImpl(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }


    public <T extends CommandExecutor> T createExecutor(Class<T> executorClass) {
        return this.createExecutor(executorClass, executorClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends CommandExecutor> T createExecutor(Class<?> entityClass, Class<T> executorClass) {
        CommandExecutor commandExecutor = this.jdbcEngineConfig.getCommandExecutor(entityClass, executorClass);
        return (T) commandExecutor;
    }

}
