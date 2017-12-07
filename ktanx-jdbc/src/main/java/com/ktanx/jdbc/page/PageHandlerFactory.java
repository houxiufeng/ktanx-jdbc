package com.ktanx.jdbc.page;

/**
 * Created by liyd on 17/4/17.
 */
public interface PageHandlerFactory {


    /**
     * 执行器支持的类型
     *
     * @return
     */
    Class<?>[] getSupportType();

    /**
     * 获取执行器
     *
     * @return
     */
    PageHandler getPageHandler();
}
