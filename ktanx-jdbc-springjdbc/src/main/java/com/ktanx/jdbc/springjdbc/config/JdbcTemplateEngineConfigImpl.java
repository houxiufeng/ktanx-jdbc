package com.ktanx.jdbc.springjdbc.config;

import com.ktanx.jdbc.config.AbstractJdbcEngineConfig;
import com.ktanx.jdbc.persist.PersistExecutorFactory;
import com.ktanx.jdbc.springjdbc.persist.JdbcTemplatePersistExecutorFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateEngineConfigImpl extends AbstractJdbcEngineConfig {

    private JdbcOperations jdbcOperations;

    protected void doInit() {

        if (this.defaultPersistExecutor == null) {
            if (jdbcOperations == null) {
                jdbcOperations = new JdbcTemplate(dataSource);
            }
            PersistExecutorFactory persistExecutorFactory = new JdbcTemplatePersistExecutorFactory(jdbcOperations);
            this.defaultPersistExecutor = persistExecutorFactory.getExecutor();
        }

        if (StringUtils.isBlank(this.dialect)) {
            this.dialect = jdbcOperations.execute(new ConnectionCallback<String>() {
                public String doInConnection(Connection con) throws SQLException, DataAccessException {
                    return con.getMetaData().getDatabaseProductName().toUpperCase();
                }
            });
        }
    }

    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
