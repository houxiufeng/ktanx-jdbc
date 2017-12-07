package com.ktanx.jdbc.command.batis.build;

/**
 * Created by liyd on 2015-11-24.
 */
public interface BatisContext {


    /**
     * 获取boundSql
     *
     * @param refId
     * @param parameters
     * @return
     */
    BoundSql getBoundSql(String refId, Object[] parameters);
}
