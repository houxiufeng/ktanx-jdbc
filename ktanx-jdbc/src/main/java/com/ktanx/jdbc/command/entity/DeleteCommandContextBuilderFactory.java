package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteCommandContextBuilderFactory extends AbstractCommandContextBuilderFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Delete.class;
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        DeleteSqlCommandContextBuilderImpl commandContextBuilder = new DeleteSqlCommandContextBuilderImpl();
        initCommandContextBuilder(commandContextBuilder, clazz, jdbcEngineConfig);
        return commandContextBuilder;
    }
}
