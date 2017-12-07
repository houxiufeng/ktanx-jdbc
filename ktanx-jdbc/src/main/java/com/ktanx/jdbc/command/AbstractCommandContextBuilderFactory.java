package com.ktanx.jdbc.command;

import com.ktanx.jdbc.command.entity.AbstractCommandContextBuilder;
import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.persist.KeyGenerator;

/**
 * Created by liyd on 17/4/22.
 */
public abstract class AbstractCommandContextBuilderFactory implements CommandContextBuilderFactory {

    /**
     * 初始化
     *
     * @param abstractCommandContextBuilder
     * @param clazz
     * @param jdbcEngineConfig
     */
    public void initCommandContextBuilder(AbstractCommandContextBuilder abstractCommandContextBuilder, Class<?> clazz, JdbcEngineConfig jdbcEngineConfig) {
        abstractCommandContextBuilder.setCommandUpperCase(jdbcEngineConfig.isCommandUpperCase());
        KeyGenerator keyGenerator = jdbcEngineConfig.getKeyGenerator(clazz);
        abstractCommandContextBuilder.setKeyGenerator(keyGenerator);
    }


}
