package com.ktanx.jdbc.springjdbc.persist;

import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.persist.AbstractJdbcDaoImpl;
import com.ktanx.jdbc.springjdbc.config.JdbcTemplateEngineConfigImpl;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateDaoImpl extends AbstractJdbcDaoImpl {

    public JdbcEngineConfig getDefaultJdbcEngineConfig() {
        return new JdbcTemplateEngineConfigImpl();
    }
}
