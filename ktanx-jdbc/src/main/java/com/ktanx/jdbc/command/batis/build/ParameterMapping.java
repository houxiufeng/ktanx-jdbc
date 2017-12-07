package com.ktanx.jdbc.command.batis.build;

/**
 * Created by liyd on 2015-11-27.
 */
public class ParameterMapping {

    private String property;
//    private String expression;

    private ParameterMapping() {
    }

    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(String property) {
            parameterMapping.property = property;
        }

        public ParameterMapping build() {
            return parameterMapping;
        }
    }

    public String getProperty() {
        return property;
    }

}
