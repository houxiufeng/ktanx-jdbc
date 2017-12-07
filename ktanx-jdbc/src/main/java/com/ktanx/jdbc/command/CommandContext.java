package com.ktanx.jdbc.command;

import com.ktanx.jdbc.datasource.DynamicDataSourceHolder;
import com.ktanx.jdbc.exception.KtanxJdbcException;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行的命令内容
 * <p>
 * Created by liyd on 17/4/12.
 */
public class CommandContext {

    private Class<?> type;

    private Class<?> resultType;

    private String pkField = "";

    private String pkColumn = "";

    private String command = "";

    private List<String> parameterNames;

    private List<Object> parameters;

    private CommandType commandType;

    private boolean pkValueByDb = true;

    private Object keyGeneratorValue;

    public CommandContext() {
        parameterNames = new ArrayList<String>();
        parameters = new ArrayList<Object>();
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
        //设置读写模式
        if (commandType.getMode() == CommandType.MODE_READ) {
            DynamicDataSourceHolder.setReadMode();
        } else if (commandType.getMode() == CommandType.MODE_WRITE) {
            DynamicDataSourceHolder.setWriteMode();
        } else {
            throw new KtanxJdbcException("不支持的模式:" + commandType.getMode());
        }
    }

    public void addParameterNames(List<String> parameterNames) {
        this.parameterNames.addAll(parameterNames);
    }

    public void addParameters(List<Object> parameters) {
        this.parameters.addAll(parameters);
    }

    public enum CommandType {

        SELECT_OBJECT(1),

        SELECT_OBJECT_LIST(1),

        SELECT_SINGLE_COLUMN(1),

        SELECT_SINGLE_COLUMN_LIST(1),

        SELECT_LIST(1),

        SELECT_SINGLE(1),

        INSERT(2),

        UPDATE(2),

        DELETE(2),

        EXECUTE(2);

        public static final int MODE_READ = 1;
        public static final int MODE_WRITE = 2;

        /**
         * 模式 1写 2读
         */
        private int mode;

        CommandType(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean isPkValueByDb() {
        return pkValueByDb;
    }

    public void setPkValueByDb(boolean pkValueByDb) {
        this.pkValueByDb = pkValueByDb;
    }

    public Object getKeyGeneratorValue() {
        return keyGeneratorValue;
    }

    public void setKeyGeneratorValue(Object keyGeneratorValue) {
        this.keyGeneratorValue = keyGeneratorValue;
    }
}
