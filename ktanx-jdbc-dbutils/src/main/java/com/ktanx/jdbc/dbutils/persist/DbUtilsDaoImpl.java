package com.ktanx.jdbc.dbutils.persist;

import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.dbutils.config.DbUtilsEngineConfigImpl;
import com.ktanx.jdbc.persist.AbstractJdbcDaoImpl;

/**
 * Created by liyd on 17/8/31.
 */
public class DbUtilsDaoImpl extends AbstractJdbcDaoImpl {

    public JdbcEngineConfig getDefaultJdbcEngineConfig() {
        return new DbUtilsEngineConfigImpl();
    }
}
