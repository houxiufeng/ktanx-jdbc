package com.ktanx.jdbc.mapping;

/**
 * Created by liyd on 17/4/11.
 */
public interface MappingHandlerFactory {


    /**
     * 支持的类
     *
     * @return
     */
    Class<?>[] getSupportClasses();

    /**
     * 获取MappingHandler
     *
     * @return
     */
    MappingHandler getMappingHandler();
}
