package com.ktanx.jdbc.management;

import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.mapping.MappingHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/11.
 */
public class CommandTable {

    /**
     * 实体类型
     */
    private Class<?> type;

    /**
     * 返回结果类型
     */
    private Class<?> resultType;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 操作的属性
     */
    private List<CommandField> operationFields;

    /**
     * where的属性/列
     */
    private List<CommandField> whereFields;

    /**
     * 白名单
     */
    private List<String> includeFields;

    /**
     * 黑名单
     */
    private List<String> excludeFields;

    /**
     * 排序属性
     */
    private List<CommandField> orderByFields;

    /**
     * 映射处理器
     */
    private MappingHandler mappingHandler;

    /**
     * 是否不查询实体属性
     */
    private boolean isNotSelectEntityField = false;

    /**
     * 是否忽略null值
     */
    private boolean isIgnoreNull = true;

    /**
     * 一些扩展的数据
     */
    private Map<String, Object> extendData;

    public CommandTable() {
        operationFields = new ArrayList<CommandField>();
        whereFields = new ArrayList<CommandField>();
        includeFields = new ArrayList<String>();
        excludeFields = new ArrayList<String>();
        orderByFields = new ArrayList<CommandField>();
    }

    public enum ExtendDataKey {

        COMMAND,

        PARAMETERS
    }

    /**
     * 添加数据
     *
     * @param key
     * @param value
     */
    public void addExtendData(String key, Object value) {
        if (this.extendData == null) {
            this.extendData = new HashMap<String, Object>();
        }
        this.extendData.put(key, value);
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public Object getExtendData(String key) {
        return this.extendData == null ? null : this.extendData.get(key);
    }

    /**
     * 添加操作的属性
     *
     * @param commandField
     */
    public void addOperationField(CommandField commandField) {
        this.operationFields.add(commandField);
    }

    /**
     * 添加操作属性
     */
    public void addWhereField(CommandField commandField) {
        this.whereFields.add(commandField);
    }

    /**
     * 设置排序
     *
     * @param sort the sort
     */
    public void setOrderBy(String sort) {
        List<CommandField> orderByFields = this.getOrderByFields();
        if (orderByFields.size() == 0) {
            throw new KtanxJdbcException("请先指定需要排序的属性");
        }
        for (int i = orderByFields.size() - 1; i >= 0; i--) {
            CommandField commandField = orderByFields.get(i);
            if (commandField.getType() != CommandField.Type.ORDER_BY || StringUtils.isNotBlank(commandField.getFieldOperator())) {
                break;
            }
            commandField.setFieldOperator(sort);
        }
    }

    /**
     * 添加排序属性
     *
     * @param commandField
     */
    public void addOrderByField(CommandField commandField) {
        this.orderByFields.add(commandField);
    }

    /**
     * 添加白名单属性
     *
     * @param fields
     */
    public void addIncludeFields(String... fields) {
        for (String field : fields) {
            this.includeFields.add(field);
        }
    }

    /**
     * 添加黑名单属性
     *
     * @param fields
     */
    public void addExcludeFields(String... fields) {
        for (String field : fields) {
            this.excludeFields.add(field);
        }
    }

    /**
     * 是否白名单属性
     *
     * @param field
     * @return
     */
    public boolean isIncludeField(String field) {
        if (this.includeFields == null || this.includeFields.isEmpty()) {
            //名单为空，默认全是白名单
            return true;
        }
        return this.includeFields.contains(field);
    }

    /**
     * 是否黑名单
     *
     * @param field
     * @return
     */
    public boolean isExcludeField(String field) {
        if (this.excludeFields == null || this.excludeFields.isEmpty()) {
            return false;
        }
        return this.excludeFields.contains(field);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public List<CommandField> getOperationFields() {
        return operationFields;
    }

    public List<CommandField> getWhereFields() {
        return whereFields;
    }

    public boolean isNotSelectEntityField() {
        return isNotSelectEntityField;
    }

    public void setNotSelectEntityField(boolean notSelectEntityField) {
        isNotSelectEntityField = notSelectEntityField;
    }

    public boolean isIgnoreNull() {
        return isIgnoreNull;
    }

    public void setIgnoreNull(boolean ignoreNull) {
        isIgnoreNull = ignoreNull;
    }

    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public List<CommandField> getOrderByFields() {
        return orderByFields;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }
}
