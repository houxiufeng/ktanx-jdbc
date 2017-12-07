package com.ktanx.jdbc.springjdbc.config;

import com.ktanx.jdbc.config.AbstractJdbcEngineConfig;
import com.ktanx.jdbc.springjdbc.persist.JdbcTemplatePersistExecutor;
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
            JdbcTemplatePersistExecutor jdbcTemplatePersistExecutor = new JdbcTemplatePersistExecutor();
            jdbcTemplatePersistExecutor.setJdbcOperations(jdbcOperations);
            this.defaultPersistExecutor = jdbcTemplatePersistExecutor;
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
