package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectCommandContextBuilderFactory extends AbstractCommandContextBuilderFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Select.class;
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        SelectSqlCommandContextBuilderImpl commandContextBuilder = new SelectSqlCommandContextBuilderImpl();
        initCommandContextBuilder(commandContextBuilder, clazz, jdbcEngineConfig);
        return commandContextBuilder;
    }
}
