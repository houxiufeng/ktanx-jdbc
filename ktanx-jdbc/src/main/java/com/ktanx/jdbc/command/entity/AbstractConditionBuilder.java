package com.ktanx.jdbc.command.entity;

import com.ktanx.common.utils.ClassUtils;
import com.ktanx.jdbc.annotation.Transient;
import com.ktanx.jdbc.command.AbstractCommandExecutor;
import com.ktanx.jdbc.management.CommandField;
import com.ktanx.jdbc.management.MappingCache;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by liyd on 17/4/11.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractConditionBuilder<T extends ConditionBuilder<T>> extends AbstractCommandExecutor implements ConditionBuilder<T> {


    public T tableAlias(String tableAlias) {
        this.commandTable.setTableAlias(tableAlias);
        return (T) this;
    }

    public T where() {
        CommandField commandField = CommandField.builder()
                .logicalOperator("where")
                .type(CommandField.Type.WHERE_ONLY)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T where(String field, Object[] value) {
        this.where(field, value == null ? "is" : "=", value);
        return (T) this;
    }

    public T where(String field, Object value) {
        Object[] values = value instanceof Object[] ? (Object[]) value : new Object[]{value};
        this.where(field, value == null ? "is" : "=", values);
        return (T) this;
    }

    public T where(String field, String operator, Object... values) {
        CommandField commandField = CommandField.builder()
                .logicalOperator("where")
                .name(field)
                .fieldOperator(operator)
                .value(values)
                .type(CommandField.Type.WHERE_FIELD)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T condition(String field, Object value) {
        Object[] values = value instanceof Object[] ? (Object[]) value : new Object[]{value};
        return this.condition(field, value == null ? "is" : "=", values);
    }

    public T condition(String field, Object[] value) {
        return this.condition(field, value == null ? "is" : "=", value);
    }

    public T condition(String field, String operator, Object... values) {
        CommandField commandField = CommandField.builder()
                .name(field)
                .fieldOperator(operator)
                .value(values)
                .type(CommandField.Type.WHERE_CONDITION)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T conditionEntity(Object entity) {
        return conditionEntity(entity, null, "and");
    }

    @Override
    public T andConditionEntity(Object entity) {
        return conditionEntity(entity, "and", "and");
    }

    @Override
    public T conditionEntity(Object entity, String wholeLogicalOperator, String fieldLogicalOperator) {

        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);

        int count = 1;
        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {

            //忽略掉null
            if (entry.getValue() == null) {
                continue;
            }

            if (count == 1 && StringUtils.isNotBlank(wholeLogicalOperator)) {
                CommandField commandField = CommandField.builder()
                        .logicalOperator(wholeLogicalOperator)
                        .type(CommandField.Type.WHERE_FIELD)
                        .orig(CommandField.Orig.ENTITY)
                        .build();
                this.commandTable.addWhereField(commandField);
            }

            CommandField commandField = CommandField.builder()
                    .logicalOperator(count > 1 ? fieldLogicalOperator : null)
                    .name(entry.getKey())
                    .fieldOperator("=")
                    .value(entry.getValue())
                    .type(CommandField.Type.WHERE_AND_FIELD)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addWhereField(commandField);
            count++;
        }

        return (T) this;
    }

    public T conditionId(Serializable value) {
        String pkField = MappingCache.getPkField(this.commandTable);
        CommandField commandField = CommandField.builder()
                .name(pkField)
                .fieldOperator("=")
                .value(value)
                .type(CommandField.Type.WHERE_FIELD)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T and() {
        CommandField commandField = CommandField.builder()
                .logicalOperator("and")
                .type(CommandField.Type.WHERE_AND_ONLY)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T and(String field, Object value) {
        Object[] values = value instanceof Object[] ? (Object[]) value : new Object[]{value};
        this.and(field, value == null ? "is" : "=", values);
        return (T) this;
    }

    public T and(String field, Object[] value) {
        this.and(field, value == null ? "is" : "=", value);
        return (T) this;
    }

    public T and(String field, String operator, Object... values) {
        CommandField commandField = CommandField.builder()
                .logicalOperator("and")
                .name(field)
                .fieldOperator(operator)
                .value(values)
                .type(CommandField.Type.WHERE_AND_FIELD)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T or() {
        CommandField commandField = CommandField.builder()
                .logicalOperator("or")
                .type(CommandField.Type.WHERE_OR_ONLY)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T or(String field, Object value) {
        Object[] values = value instanceof Object[] ? (Object[]) value : new Object[]{value};
        return this.or(field, value == null ? "is" : "=", values);
    }

    public T or(String field, Object[] values) {
        return this.or(field, values == null ? "is" : "=", values);
    }

    public T or(String field, String operator, Object... values) {
        CommandField commandField = CommandField.builder()
                .logicalOperator("or")
                .name(field)
                .fieldOperator(operator)
                .value(values)
                .type(CommandField.Type.WHERE_OR_FIELD)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T begin() {
        CommandField commandField = CommandField.builder()
                .logicalOperator("(")
                .type(CommandField.Type.WHERE_BEGIN)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }

    public T end() {
        CommandField commandField = CommandField.builder()
                .logicalOperator(")")
                .type(CommandField.Type.WHERE_END)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addWhereField(commandField);
        return (T) this;
    }
}
