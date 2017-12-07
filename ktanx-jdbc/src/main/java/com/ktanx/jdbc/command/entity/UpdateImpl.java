package com.ktanx.jdbc.command.entity;

import com.ktanx.common.utils.ClassUtils;
import com.ktanx.jdbc.annotation.Transient;
import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.management.CommandField;
import com.ktanx.jdbc.management.MappingCache;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class UpdateImpl<T> extends AbstractConditionBuilder<Update<T>> implements Update<T> {


    public Update<T> set(String field, Object value) {
        CommandField commandField = CommandField.builder()
                .name(field)
                .value(value)
                .type(CommandField.Type.UPDATE)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addOperationField(commandField);
        return this;
    }

    public Update<T> setForEntityWhereId(Object entity) {
        String pkField = MappingCache.getPkField(this.commandTable);

        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        //处理主键成where条件
        Object pkValue = beanPropMap.get(pkField);
        if (pkValue == null) {
            throw new KtanxJdbcException("主键属性值不能为空:" + pkField);
        }

        CommandField pkCommandField = CommandField.builder()
                .logicalOperator("where")
                .name(pkField)
                .fieldOperator("=")
                .value(pkValue)
                .type(CommandField.Type.WHERE_FIELD)
                .orig(CommandField.Orig.ENTITY)
                .build();
        this.commandTable.addWhereField(pkCommandField);
        //移除
        beanPropMap.remove(pkField);

        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.UPDATE)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Update<T> setForEntity(Object entity) {
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);
        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
            //不忽略null，最后构建时根据updateNull设置处理null值
            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.UPDATE)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Update<T> updateNull() {
        this.commandTable.setIgnoreNull(false);
        return this;
    }

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.UPDATE);
        return (Integer) this.persistExecutor.execute(commandContext);
    }
}
