package com.ktanx.jdbc.command.natives;

import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.command.simple.SimpleCommandContextBuilder;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class NativeExecutorCommandContextBuilderFactory implements CommandContextBuilderFactory {

    private CommandContextBuilder commandContextBuilder;

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return NativeExecutor.class;
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        if (this.commandContextBuilder == null) {
            this.commandContextBuilder = new SimpleCommandContextBuilder();
        }
        return this.commandContextBuilder;
    }
}
