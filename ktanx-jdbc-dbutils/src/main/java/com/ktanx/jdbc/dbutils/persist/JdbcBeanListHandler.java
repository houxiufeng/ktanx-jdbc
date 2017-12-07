package com.ktanx.jdbc.dbutils.persist;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 17/8/31.
 */
public class JdbcBeanListHandler<T> implements ResultSetHandler<List<T>> {

    private final Class<T> type;

    private final JdbcRowProcessor<T> rowProcessor;

    public JdbcBeanListHandler(Class<T> type) {
        this(type, new DefaultRowProcessor<T>(type));
    }

    public JdbcBeanListHandler(Class<T> type, JdbcRowProcessor rowProcessor) {
        this.type = type;
        this.rowProcessor = rowProcessor;
    }

    public List<T> handle(ResultSet rs) throws SQLException {

        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T t = this.rowProcessor.toBean(rs, type);
            list.add(t);
        }
        return list;
    }

    public static <T> JdbcBeanListHandler<T> newInstance(Class<T> type) {
        return new JdbcBeanListHandler<T>(type);
    }
}
