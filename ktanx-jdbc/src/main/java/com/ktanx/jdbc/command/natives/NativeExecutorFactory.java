package com.ktanx.jdbc.command.natives;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class NativeExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {

        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return NativeExecutor.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        NativeExecutorImpl nativeExecutor = new NativeExecutorImpl();
        this.initCommandExecutor(nativeExecutor, clazz, NativeExecutor.class, jdbcEngineConfig);
        return nativeExecutor;
    }
}
