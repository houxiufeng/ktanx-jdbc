package com.ktanx.jdbc.command.batis;

import com.ktanx.jdbc.command.AbstractCommandExecutorFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/25.
 */
public class BatisExecutorFactory extends AbstractCommandExecutorFactory {

    public Class<?>[] getSupportType() {

        return new Class<?>[]{Object.class};
    }

    public Class<? extends CommandExecutor> getExecutorInterface() {
        return BatisExecutor.class;
    }

    public <T> CommandExecutor getExecutor(Class<T> clazz, JdbcEngineConfig jdbcEngineConfig) {
        BatisExecutorImpl batisExecutor = new BatisExecutorImpl();
        this.initCommandExecutor(batisExecutor, clazz, BatisExecutor.class, jdbcEngineConfig);
        return batisExecutor;
    }
}
