package com.ktanx.jdbc.springjdbc.persist;

import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.persist.PersistExecutor;
import com.ktanx.jdbc.persist.PersistExecutorFactory;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * JdbcTemplate实现
 * <p>
 * Created by liyd on 17/4/17.
 */
public class JdbcTemplatePersistExecutorFactory implements PersistExecutorFactory {

    private JdbcOperations jdbcOperations;

    private PersistExecutor persistExecutor;

    public JdbcTemplatePersistExecutorFactory() {
    }

    public JdbcTemplatePersistExecutorFactory(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    public Class<?>[] getSupportType() {
        return new Class<?>[]{Object.class};
    }

    public PersistExecutor getExecutor() {
        if (this.persistExecutor == null) {
            JdbcTemplatePersistExecutor jdbcTemplatePersistExecutor = new JdbcTemplatePersistExecutor();
            if (this.jdbcOperations == null) {
                throw new KtanxJdbcException("jdbcOperations不能为空");
            }
            jdbcTemplatePersistExecutor.setJdbcOperations(this.jdbcOperations);
            this.persistExecutor = jdbcTemplatePersistExecutor;
        }
        return this.persistExecutor;
    }

    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
