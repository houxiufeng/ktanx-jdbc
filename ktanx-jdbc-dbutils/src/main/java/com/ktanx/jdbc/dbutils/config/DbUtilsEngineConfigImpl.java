package com.ktanx.jdbc.dbutils.config;

import com.ktanx.jdbc.config.AbstractJdbcEngineConfig;
import com.ktanx.jdbc.dbutils.persist.DbUtilsPersistExecutorFactory;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.persist.PersistExecutorFactory;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

/**
 * Created by liyd on 17/8/31.
 */
public class DbUtilsEngineConfigImpl extends AbstractJdbcEngineConfig {


    protected void doInit() {

        if (this.defaultPersistExecutor == null) {
            PersistExecutorFactory persistExecutorFactory = new DbUtilsPersistExecutorFactory(getDataSource());
            this.defaultPersistExecutor = persistExecutorFactory.getExecutor();
        }

        if (StringUtils.isBlank(this.dialect)) {
            try {
                this.dialect = getDataSource().getConnection().getMetaData().getDatabaseProductName().toUpperCase();
            } catch (SQLException e) {
                throw new KtanxJdbcException("获取数据库dialect失败", e);
            }
        }
    }
}
