package com.ktanx.jdbc.dbutils.persist;

import com.ktanx.jdbc.persist.PersistExecutor;
import com.ktanx.jdbc.persist.PersistExecutorFactory;

import javax.sql.DataSource;

/**
 * Created by liyd on 17/8/31.
 */
public class DbUtilsPersistExecutorFactory implements PersistExecutorFactory {


    private DataSource dataSource;

    private PersistExecutor persistExecutor;

    public DbUtilsPersistExecutorFactory(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public PersistExecutor getExecutor() {
        if (this.persistExecutor == null) {
            DbUtilsPersistExecutor dbUtilsPersistExecutor = new DbUtilsPersistExecutor();
            dbUtilsPersistExecutor.setDataSource(this.dataSource);
            this.persistExecutor = dbUtilsPersistExecutor;
        }
        return this.persistExecutor;
    }
}
