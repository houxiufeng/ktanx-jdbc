package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectCommandExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Select.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        SelectImpl<T> select = new SelectImpl<T>();
        this.initCommandExecutor(select, clazz, Select.class, jdbcEngineConfig);
        return select;
    }
}
