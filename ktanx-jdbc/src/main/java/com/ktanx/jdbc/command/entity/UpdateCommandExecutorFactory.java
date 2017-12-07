package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateCommandExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Update.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        UpdateImpl<T> update = new UpdateImpl<T>();
        this.initCommandExecutor(update, clazz, Update.class, jdbcEngineConfig);
        return update;
    }
}
