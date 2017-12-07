package com.ktanx.jdbc.command.batis;

import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class BatisExecutorCommandContextBuilderFactory implements CommandContextBuilderFactory {

    private CommandContextBuilder commandContextBuilder;

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return BatisExecutor.class;
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        if (this.commandContextBuilder == null) {
            this.commandContextBuilder = new BatisExecutorCommandContextBuilder(jdbcEngineConfig.getBatisContext());
        }
        return this.commandContextBuilder;
    }
}
