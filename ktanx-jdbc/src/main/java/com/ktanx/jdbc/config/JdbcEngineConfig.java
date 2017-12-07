package com.ktanx.jdbc.config;

import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.command.batis.build.BatisContext;
import com.ktanx.jdbc.mapping.MappingHandler;
import com.ktanx.jdbc.page.PageHandler;
import com.ktanx.jdbc.persist.KeyGenerator;
import com.ktanx.jdbc.persist.PersistExecutor;

import javax.sql.DataSource;

/**
 * jdbc配置类
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface JdbcEngineConfig {

    /**
     * 获取数据库方言
     *
     * @return
     */
    String getDialect();

    /**
     * 获取CommandExecutor
     *
     * @param factType
     * @param executorInterface
     * @return
     */
    CommandExecutor getCommandExecutor(Class<?> factType, Class<?> executorInterface);

    /**
     * 获取CommandContextBuilder
     *
     * @param factType          the fact type
     * @param executorInterface the executor interface
     * @return command context builder factory
     */
    CommandContextBuilder getCommandContextBuilder(Class<?> factType, Class<?> executorInterface);


    /**
     * 获取PersistExecutor
     * 先根据type获取对应的PersistExecutor，如果没有，再根据topInterfaceClass获取对应的PersistExecutor
     *
     * @param type 对应的类型
     * @return persist executor
     */
    PersistExecutor getPersistExecutor(Class<?> type);

    /**
     * 获取MappingHandler
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @param type 对应的类型
     * @return mapping handler
     */
    MappingHandler getMappingHandler(Class<?> type);

    /**
     * 获取PageHandler
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @param type 对应的类型
     * @return page handler
     */
    PageHandler getPageHandler(Class<?> type);

    /**
     * 获取KeyGenerator
     * 先根据type获取，如果没有再根据topInterfaceClass获取
     *
     * @param type the type
     * @return key generator
     */
    KeyGenerator getKeyGenerator(Class<?> type);

    /**
     * command是否大写
     *
     * @return
     */
    boolean isCommandUpperCase();

    /**
     * 获取batis上下文
     *
     * @return
     */
    BatisContext getBatisContext();

    /**
     * 获取JdbcEngine
     *
     * @param dataSource the data source
     * @return jdbc engine
     */
    JdbcEngine buildJdbcEngine(DataSource dataSource);

}
