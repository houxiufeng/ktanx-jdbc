package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteCommandExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Delete.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        DeleteImpl<T> delete = new DeleteImpl<T>();
        this.initCommandExecutor(delete, clazz, Delete.class, jdbcEngineConfig);
        return delete;
    }
}
