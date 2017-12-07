package com.ktanx.jdbc.command.batis.build;

import com.ktanx.jdbc.command.batis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-11-25.
 */
public class BatisBoundSql implements BoundSql {

    private String sql;
    private List<ParameterMapping> parameterMappings;
//    private Object parameterObject;
    private Map<String, Object> additionalParameters;
    private MetaObject metaParameters;

    public BatisBoundSql(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
//        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<String, Object>();
        this.metaParameters = MetaObject.forObject(additionalParameters);
    }

    public List<Object> getParameters() {
        List<Object> params = new ArrayList<Object>();
        if (parameterMappings == null) {
            return params;
        }
        for (ParameterMapping parameterMapping : parameterMappings) {
            String property = parameterMapping.getProperty();
            Object value = metaParameters.getValue(property);
            params.add(value);
        }
        return params;
    }

    public String getSql() {
        return sql;
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

}
