package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertCommandExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Insert.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        InsertImpl<T> insert = new InsertImpl<T>();
        this.initCommandExecutor(insert, clazz, Insert.class, jdbcEngineConfig);
        return insert;
    }

}
