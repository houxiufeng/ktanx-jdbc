package com.ktanx.jdbc.command.batis.build;

import com.ktanx.jdbc.exception.KtanxJdbcException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyd on 2015-11-25.
 */
public class DynamicContext {

    private final Map<String, Object> bindings;
    private final StringBuilder sqlBuilder = new StringBuilder();
    private int uniqueNumber = 0;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public DynamicContext(Object parameterObject) {
        bindings = new HashMap<String, Object>();
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            throw new KtanxJdbcException("参数错误");
        }
        if (parameterObject instanceof Map) {
            bindings.putAll((Map) parameterObject);
        }
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

}
