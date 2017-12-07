package com.ktanx.jdbc.dbutils.persist;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by liyd on 17/8/31.
 */
public class JdbcBeanHandler<T> implements ResultSetHandler<T> {

    private final Class<T> type;

    private final JdbcRowProcessor<T> rowProcessor;

    public JdbcBeanHandler(Class<T> type) {
        this(type, new DefaultRowProcessor<>(type));
    }

    public JdbcBeanHandler(Class<T> type, JdbcRowProcessor<T> rowProcessor) {
        this.type = type;
        this.rowProcessor = rowProcessor;
    }

    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.rowProcessor.toBean(rs, type) : null;
    }

    public static <T> JdbcBeanHandler<T> newInstance(Class<T> type) {
        return new JdbcBeanHandler<>(type);
    }
}
