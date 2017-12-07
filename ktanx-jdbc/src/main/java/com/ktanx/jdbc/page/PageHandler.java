package com.ktanx.jdbc.page;

import com.ktanx.common.model.Pager;

/**
 * Created by liyd on 16/6/8.
 */
public interface PageHandler {

    /**
     * 根据查询语句获取查count语句
     *
     * @param command
     * @param dialect
     * @return
     */
    String getCountCommand(String command, String dialect);

    /**
     * 根据查询语句获取分页语句
     *
     * @param command
     * @param pager
     * @param dialect
     * @return
     */
    String getPageCommand(String command, Pager pager, String dialect);
}
