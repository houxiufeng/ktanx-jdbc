package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.AbstractCommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertCommandContextBuilderFactory extends AbstractCommandContextBuilderFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return Insert.class;
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        InsertSqlCommandContextBuilderImpl commandContextBuilder = new InsertSqlCommandContextBuilderImpl();
        initCommandContextBuilder(commandContextBuilder, clazz, jdbcEngineConfig);
        return commandContextBuilder;
    }
}
