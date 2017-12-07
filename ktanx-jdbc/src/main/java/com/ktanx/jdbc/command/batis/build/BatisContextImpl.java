package com.ktanx.jdbc.command.batis.build;

import com.ktanx.jdbc.exception.KtanxJdbcException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyd on 17/5/3.
 */
public class BatisContextImpl implements BatisContext {

    /**
     * 默认参数名
     */
    private static final String DEFAULT_PARAMETERS_KEY = "params";

    private Configuration configuration;

    public BatisContextImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    public BoundSql getBoundSql(String refId, Object[] parameters) {

        Map<String, Object> params = this.processParameters(parameters);
        MappedStatement mappedStatement = this.configuration.getMappedStatements().get(refId);
        if (mappedStatement == null) {
            throw new KtanxJdbcException("引用的对象没有找到,refId=" + refId);
        }
        return mappedStatement.getSqlSource().getBoundSql(params);
    }

    /**
     * 处理转换参数
     *
     * @param parameters
     * @return
     */
    private Map<String, Object> processParameters(Object[] parameters) {

        if (ArrayUtils.isEmpty(parameters)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (parameters.length == 1) {
            map.put(DEFAULT_PARAMETERS_KEY, parameters[0]);
        } else {
            map.put(DEFAULT_PARAMETERS_KEY, parameters);
        }
        return map;
    }
}
