package com.ktanx.jdbc.dbutils.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by liyd on 17/8/31.
 */
public interface JdbcRowProcessor<T> {

    <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

}
