package com.ktanx.jdbc.config;

import com.ktanx.jdbc.command.CommandContextBuilder;
import com.ktanx.jdbc.command.CommandContextBuilderFactory;
import com.ktanx.jdbc.command.CommandExecutor;
import com.ktanx.jdbc.command.CommandExecutorFactory;
import com.ktanx.jdbc.command.batis.BatisExecutorCommandContextBuilderFactory;
import com.ktanx.jdbc.command.batis.BatisExecutorFactory;
import com.ktanx.jdbc.command.batis.build.BatisContext;
import com.ktanx.jdbc.command.batis.build.BatisContextFactory;
import com.ktanx.jdbc.command.entity.*;
import com.ktanx.jdbc.command.natives.NativeExecutorCommandContextBuilderFactory;
import com.ktanx.jdbc.command.natives.NativeExecutorFactory;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.mapping.DefaultMappingHandler;
import com.ktanx.jdbc.mapping.MappingHandler;
import com.ktanx.jdbc.mapping.MappingHandlerFactory;
import com.ktanx.jdbc.page.PageHandler;
import com.ktanx.jdbc.page.PageHandlerFactory;
import com.ktanx.jdbc.page.SimplePageHandler;
import com.ktanx.jdbc.persist.KeyGenerator;
import com.ktanx.jdbc.persist.KeyGeneratorFactory;
import com.ktanx.jdbc.persist.PersistExecutor;
import com.ktanx.jdbc.persist.PersistExecutorFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/11.
 */
public abstract class AbstractJdbcEngineConfig implements JdbcEngineConfig {

    /**
     * 数据源
     */
    protected DataSource dataSource;

    /**
     * 数据库
     */
    protected String dialect;

    /**
     * 执行器map
     */
    protected Map<Class<?>, Map<Class<? extends CommandExecutor>, CommandExecutorFactory>> commandExecutorFactoryMap;

    /**
     * CommandContext构建器map
     */
    protected Map<Class<?>, Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory>> commandContextBuilderFactoriesMap;

    /**
     * 映射处理器map
     */
    protected Map<Class<?>, MappingHandlerFactory> mappingHandlerFactoriesMap;

    /**
     * 持久化执行器map
     */
    protected Map<Class<?>, PersistExecutorFactory> persistExecutorFactoriesMap;

    /**
     * 分页处理器map
     */
    protected Map<Class<?>, PageHandlerFactory> pageHandlerFactoriesMap;

    /**
     * 主键生成器map
     */
    protected Map<Class<?>, KeyGeneratorFactory> keyGeneratorFactoriesMap;

    /**
     * batis配置文件
     */
    protected String batisConfigLocation;

    /**
     * batis上下文
     */
    protected BatisContext batisContext;

    /**
     * 自定义执行器工厂
     */
    protected List<? extends CommandExecutorFactory> customCommandExecutorFactories;

    /**
     * 自定义CommandContext构建工厂
     */
    protected List<? extends CommandContextBuilderFactory> customCommandContextBuilderFactories;

    /**
     * 自定义映射处理器
     */
    protected List<? extends MappingHandlerFactory> customMappingHandlerFactories;

    /**
     * 自定义持久化执行器
     */
    protected List<PersistExecutorFactory> customPersistExecutorFactories;

    /**
     * 自定义分页处理器
     */
    protected List<PageHandlerFactory> customPageHandlerFactories;

    /**
     * 自定义主键生成器
     */
    protected List<KeyGeneratorFactory> customKeyGeneratorFactories;

    /**
     * 默认持久化处理
     */
    protected PersistExecutor defaultPersistExecutor;

    /**
     * 默认映射处理
     */
    protected MappingHandler defaultMappingHandler;

    /**
     * 分页处理器
     */
    protected PageHandler defaultPageHandler;

    /**
     * 默认主键生成器
     */
    protected KeyGenerator defaultKeyGenerator;

    /**
     * command是否大写
     */
    protected boolean isCommandUpperCase;

    /**
     * 初始化
     */
    protected void init() {
        if (dataSource == null) {
            throw new KtanxJdbcException("dataSource不能为空");
        }
        if (defaultMappingHandler == null) {
            defaultMappingHandler = new DefaultMappingHandler();
        }
        if (defaultPageHandler == null) {
            defaultPageHandler = new SimplePageHandler();
        }
        initCommandExecutorFactories();
        initCommandContextBuilderFactories();
        initMappingHandlerFactories();
        initPageHandlerFactories();
        initPersistExecutorFactories();
        initKeyGenerator();
        initBatisContext();

        this.doInit();
    }

    /**
     * 子类初始化
     */
    protected abstract void doInit();

    public AbstractJdbcEngineConfig() {
        commandExecutorFactoryMap = new HashMap<Class<?>, Map<Class<? extends CommandExecutor>, CommandExecutorFactory>>();
        commandContextBuilderFactoriesMap = new HashMap<Class<?>, Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory>>();
        mappingHandlerFactoriesMap = new HashMap<Class<?>, MappingHandlerFactory>();
        persistExecutorFactoriesMap = new HashMap<Class<?>, PersistExecutorFactory>();
        pageHandlerFactoriesMap = new HashMap<Class<?>, PageHandlerFactory>();
        keyGeneratorFactoriesMap = new HashMap<Class<?>, KeyGeneratorFactory>();
    }

    public CommandExecutor getCommandExecutor(Class<?> factType, Class<?> executorInterface) {

        Map<Class<? extends CommandExecutor>, CommandExecutorFactory> classCommandExecutorFactoryMap = this.commandExecutorFactoryMap.get(factType);

        if (classCommandExecutorFactoryMap == null || !classCommandExecutorFactoryMap.containsKey(executorInterface)) {
            classCommandExecutorFactoryMap = commandExecutorFactoryMap.get(Object.class);
        }

        CommandExecutorFactory commandExecutorFactory = classCommandExecutorFactoryMap.get(executorInterface);
        if (commandExecutorFactory == null) {
            throw new KtanxJdbcException("未找到相应的CommandExecutorFactory,executor:" + executorInterface.getName());
        }
        return commandExecutorFactory.getExecutor(factType, this);
    }

    public CommandContextBuilder getCommandContextBuilder(Class<?> factType, Class<?> executorInterface) {
        Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory> classCommandContextBuilderFactoryMap = commandContextBuilderFactoriesMap.get(factType);

        if (classCommandContextBuilderFactoryMap == null || !classCommandContextBuilderFactoryMap.containsKey(executorInterface)) {
            classCommandContextBuilderFactoryMap = commandContextBuilderFactoriesMap.get(Object.class);
        }

        CommandContextBuilderFactory commandContextBuilderFactory = classCommandContextBuilderFactoryMap.get(executorInterface);

        if (commandContextBuilderFactory == null) {
            throw new KtanxJdbcException("未找到相应的CommandContextBuilderFactory,executor:" + executorInterface.getName());
        }

        CommandContextBuilder commandContextBuilder = commandContextBuilderFactory.getCommandContextBuilder(factType, this);

        return commandContextBuilder;
    }

    public PersistExecutor getPersistExecutor(Class<?> type) {
        PersistExecutorFactory persistExecutorFactory = persistExecutorFactoriesMap.get(type);
        return persistExecutorFactory == null ? this.getDefaultPersistExecutor() : persistExecutorFactory.getExecutor();
    }

    public MappingHandler getMappingHandler(Class<?> type) {
        MappingHandlerFactory mappingHandlerFactory = mappingHandlerFactoriesMap.get(type);
        return mappingHandlerFactory == null ? this.getDefaultMappingHandler() : mappingHandlerFactory.getMappingHandler();
    }

    public PageHandler getPageHandler(Class<?> type) {
        PageHandlerFactory pageHandlerFactory = this.pageHandlerFactoriesMap.get(type);
        return pageHandlerFactory == null ? this.getDefaultPageHandler() : pageHandlerFactory.getPageHandler();
    }

    public KeyGenerator getKeyGenerator(Class<?> type) {
        KeyGeneratorFactory keyGeneratorFactory = this.keyGeneratorFactoriesMap.get(type);
        return keyGeneratorFactory == null ? this.getDefaultKeyGenerator() : keyGeneratorFactory.getKeyGenerator();
    }

    public boolean isCommandUpperCase() {
        return this.isCommandUpperCase;
    }

    public BatisContext getBatisContext() {
        return this.batisContext;
    }

    public JdbcEngine buildJdbcEngine(DataSource dataSource) {
        this.dataSource = dataSource;
        this.init();
        return new JdbcEngineImpl(this);
    }

    protected void initCommandExecutorFactories() {
        //默认CommandExecutorFactory
        Map<Class<? extends CommandExecutor>, CommandExecutorFactory> defaultCommandExecutorFactories = new HashMap<Class<? extends CommandExecutor>, CommandExecutorFactory>();
        SelectCommandExecutorFactory selectCommandExecutorFactory = new SelectCommandExecutorFactory();
        defaultCommandExecutorFactories.put(selectCommandExecutorFactory.getExecutorInterface(), selectCommandExecutorFactory);

        InsertCommandExecutorFactory insertCommandExecutorFactory = new InsertCommandExecutorFactory();
        defaultCommandExecutorFactories.put(insertCommandExecutorFactory.getExecutorInterface(), insertCommandExecutorFactory);

        DeleteCommandExecutorFactory deleteCommandExecutorFactory = new DeleteCommandExecutorFactory();
        defaultCommandExecutorFactories.put(deleteCommandExecutorFactory.getExecutorInterface(), deleteCommandExecutorFactory);

        UpdateCommandExecutorFactory updateCommandExecutorFactory = new UpdateCommandExecutorFactory();
        defaultCommandExecutorFactories.put(updateCommandExecutorFactory.getExecutorInterface(), updateCommandExecutorFactory);

        //native
        NativeExecutorFactory nativeExecutorFactory = new NativeExecutorFactory();
        defaultCommandExecutorFactories.put(nativeExecutorFactory.getExecutorInterface(), nativeExecutorFactory);

        //batis
        BatisExecutorFactory batisExecutorFactory = new BatisExecutorFactory();
        defaultCommandExecutorFactories.put(batisExecutorFactory.getExecutorInterface(), batisExecutorFactory);

        commandExecutorFactoryMap.put(Object.class, defaultCommandExecutorFactories);

        if (customCommandExecutorFactories != null) {
            for (CommandExecutorFactory customCommandExecutorFactory : customCommandExecutorFactories) {
                Class<?>[] supportTypes = customCommandExecutorFactory.getSupportType();
                for (Class<?> supportType : supportTypes) {
                    Map<Class<? extends CommandExecutor>, CommandExecutorFactory> commandExecutorFactories = commandExecutorFactoryMap.get(supportType);
                    if (commandExecutorFactories == null) {
                        commandExecutorFactories = new HashMap<Class<? extends CommandExecutor>, CommandExecutorFactory>();
                        commandExecutorFactoryMap.put(supportType, commandExecutorFactories);
                    }
                    commandExecutorFactories.put(customCommandExecutorFactory.getExecutorInterface(), customCommandExecutorFactory);
                }
            }
        }
    }

    protected void initCommandContextBuilderFactories() {
        Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory> defaultCommandContextBuilderFactories = new HashMap<Class<? extends CommandExecutor>, CommandContextBuilderFactory>();
        SelectCommandContextBuilderFactory selectCommandContextBuilderFactory = new SelectCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(selectCommandContextBuilderFactory.getExecutorInterface(), selectCommandContextBuilderFactory);

        InsertCommandContextBuilderFactory insertCommandContextBuilderFactory = new InsertCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(insertCommandContextBuilderFactory.getExecutorInterface(), insertCommandContextBuilderFactory);

        DeleteCommandContextBuilderFactory deleteCommandContextBuilderFactory = new DeleteCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(deleteCommandContextBuilderFactory.getExecutorInterface(), deleteCommandContextBuilderFactory);

        UpdateCommandContextBuilderFactory updateCommandContextBuilderFactory = new UpdateCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(updateCommandContextBuilderFactory.getExecutorInterface(), updateCommandContextBuilderFactory);

        //native
        NativeExecutorCommandContextBuilderFactory nativeExecutorCommandContextBuilderFactory = new NativeExecutorCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(nativeExecutorCommandContextBuilderFactory.getExecutorInterface(), nativeExecutorCommandContextBuilderFactory);

        //batis
        BatisExecutorCommandContextBuilderFactory batisExecutorCommandContextBuilderFactory = new BatisExecutorCommandContextBuilderFactory();
        defaultCommandContextBuilderFactories.put(batisExecutorCommandContextBuilderFactory.getExecutorInterface(), batisExecutorCommandContextBuilderFactory);

        commandContextBuilderFactoriesMap.put(Object.class, defaultCommandContextBuilderFactories);


        if (customCommandContextBuilderFactories != null) {

            for (CommandContextBuilderFactory customCommandContextBuilderFactory : customCommandContextBuilderFactories) {
                Class<?>[] supportTypes = customCommandContextBuilderFactory.getSupportType();
                for (Class<?> supportType : supportTypes) {
                    Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory> classCommandContextBuilderFactoryMap = commandContextBuilderFactoriesMap.get(supportType);
                    if (classCommandContextBuilderFactoryMap == null) {
                        classCommandContextBuilderFactoryMap = new HashMap<Class<? extends CommandExecutor>, CommandContextBuilderFactory>();
                        commandContextBuilderFactoriesMap.put(supportType, classCommandContextBuilderFactoryMap);
                    }
                    classCommandContextBuilderFactoryMap.put(customCommandContextBuilderFactory.getExecutorInterface(), customCommandContextBuilderFactory);
                }
            }
        }
    }

    protected void initMappingHandlerFactories() {
        if (customMappingHandlerFactories != null) {
            for (MappingHandlerFactory customMappingHandlerFactory : customMappingHandlerFactories) {
                Class<?>[] supportClasses = customMappingHandlerFactory.getSupportClasses();
                for (Class<?> supportClass : supportClasses) {
                    mappingHandlerFactoriesMap.put(supportClass, customMappingHandlerFactory);
                }
            }
        }
    }

    protected void initPageHandlerFactories() {
        if (customPageHandlerFactories != null) {
            for (PageHandlerFactory customPageHandlerFactory : customPageHandlerFactories) {
                Class<?>[] supportTypes = customPageHandlerFactory.getSupportType();
                for (Class<?> supportType : supportTypes) {
                    pageHandlerFactoriesMap.put(supportType, customPageHandlerFactory);
                }
            }
        }
    }

    protected void initPersistExecutorFactories() {
        if (customPersistExecutorFactories != null) {
            for (PersistExecutorFactory customPersistExecutorFactory : customPersistExecutorFactories) {
                Class<?>[] supportType = customPersistExecutorFactory.getSupportType();
                for (Class<?> aClass : supportType) {
                    persistExecutorFactoriesMap.put(aClass, customPersistExecutorFactory);
                }
            }
        }
    }

    protected void initKeyGenerator() {
        if (customKeyGeneratorFactories != null) {
            for (KeyGeneratorFactory customKeyGeneratorFactory : customKeyGeneratorFactories) {
                Class<?>[] supportTypes = customKeyGeneratorFactory.getSupportType();
                for (Class<?> supportType : supportTypes) {
                    keyGeneratorFactoriesMap.put(supportType, customKeyGeneratorFactory);
                }
            }
        }
    }

    protected void initBatisContext() {
        BatisContextFactory batisContextFactory = new BatisContextFactory(this.batisConfigLocation, this);
        this.batisContext = batisContextFactory.buildBatisContext();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Map<Class<?>, Map<Class<? extends CommandExecutor>, CommandContextBuilderFactory>> getCommandContextBuilderFactoriesMap() {
        return commandContextBuilderFactoriesMap;
    }

    public Map<Class<?>, Map<Class<? extends CommandExecutor>, CommandExecutorFactory>> getCommandExecutorFactoryMap() {
        return commandExecutorFactoryMap;
    }

    public Map<Class<?>, MappingHandlerFactory> getMappingHandlerFactoriesMap() {
        return mappingHandlerFactoriesMap;
    }

    public Map<Class<?>, PersistExecutorFactory> getPersistExecutorFactoriesMap() {
        return persistExecutorFactoriesMap;
    }

    public List<? extends CommandExecutorFactory> getCustomCommandExecutorFactories() {
        return customCommandExecutorFactories;
    }

    public void setCustomCommandExecutorFactories(List<? extends CommandExecutorFactory> customCommandExecutorFactories) {
        this.customCommandExecutorFactories = customCommandExecutorFactories;
    }

    public List<? extends CommandContextBuilderFactory> getCustomCommandContextBuilderFactories() {
        return customCommandContextBuilderFactories;
    }

    public void setCustomCommandContextBuilderFactories(List<? extends CommandContextBuilderFactory> customCommandContextBuilderFactories) {
        this.customCommandContextBuilderFactories = customCommandContextBuilderFactories;
    }

    public List<? extends MappingHandlerFactory> getCustomMappingHandlerFactories() {
        return customMappingHandlerFactories;
    }

    public void setCustomMappingHandlerFactories(List<? extends MappingHandlerFactory> customMappingHandlerFactories) {
        this.customMappingHandlerFactories = customMappingHandlerFactories;
    }

    public List<PersistExecutorFactory> getCustomPersistExecutorFactories() {
        return customPersistExecutorFactories;
    }

    public void setCustomPersistExecutorFactories(List<PersistExecutorFactory> customPersistExecutorFactories) {
        this.customPersistExecutorFactories = customPersistExecutorFactories;
    }

    public PersistExecutor getDefaultPersistExecutor() {
        return defaultPersistExecutor;
    }

    public void setDefaultPersistExecutor(PersistExecutor defaultPersistExecutor) {
        this.defaultPersistExecutor = defaultPersistExecutor;
    }

    public MappingHandler getDefaultMappingHandler() {
        return defaultMappingHandler;
    }

    public void setDefaultMappingHandler(MappingHandler defaultMappingHandler) {
        this.defaultMappingHandler = defaultMappingHandler;
    }

    public PageHandler getDefaultPageHandler() {
        return defaultPageHandler;
    }

    public void setDefaultPageHandler(PageHandler defaultPageHandler) {
        this.defaultPageHandler = defaultPageHandler;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public void setIsCommandUpperCase(boolean commandUpperCase) {
        isCommandUpperCase = commandUpperCase;
    }

    public KeyGenerator getDefaultKeyGenerator() {
        return defaultKeyGenerator;
    }

    public void setDefaultKeyGenerator(KeyGenerator defaultKeyGenerator) {
        this.defaultKeyGenerator = defaultKeyGenerator;
    }

    public Map<Class<?>, PageHandlerFactory> getPageHandlerFactoriesMap() {
        return pageHandlerFactoriesMap;
    }

    public Map<Class<?>, KeyGeneratorFactory> getKeyGeneratorFactoriesMap() {
        return keyGeneratorFactoriesMap;
    }


    public List<PageHandlerFactory> getCustomPageHandlerFactories() {
        return customPageHandlerFactories;
    }

    public void setCustomPageHandlerFactories(List<PageHandlerFactory> customPageHandlerFactories) {
        this.customPageHandlerFactories = customPageHandlerFactories;
    }

    public List<KeyGeneratorFactory> getCustomKeyGeneratorFactories() {
        return customKeyGeneratorFactories;
    }

    public void setCustomKeyGeneratorFactories(List<KeyGeneratorFactory> customKeyGeneratorFactories) {
        this.customKeyGeneratorFactories = customKeyGeneratorFactories;
    }

    public void setBatisConfigLocation(String batisConfigLocation) {
        this.batisConfigLocation = batisConfigLocation;
    }
}
