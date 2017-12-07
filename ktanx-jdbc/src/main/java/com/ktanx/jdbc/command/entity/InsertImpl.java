package com.ktanx.jdbc.command.entity;

import com.ktanx.common.utils.ClassUtils;
import com.ktanx.jdbc.annotation.Transient;
import com.ktanx.jdbc.command.AbstractCommandExecutor;
import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.management.CommandField;

import java.util.Map;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertImpl<T extends Object> extends AbstractCommandExecutor implements Insert<T> {


    public Insert<T> set(String field, Object value) {
        CommandField commandField = CommandField.builder()
                .name(field)
                .value(value)
                .type(CommandField.Type.INSERT)
                .orig(CommandField.Orig.MANUAL)
                .build();
        this.commandTable.addOperationField(commandField);
        return this;
    }

    public Insert<T> setForEntity(Object entity) {
        Map<String, Object> beanPropMap = ClassUtils.getSelfBeanPropMap(entity, Transient.class);

        for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {

            //忽略掉null
            if (entry.getValue() == null) {
                continue;
            }

            CommandField commandField = CommandField.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .type(CommandField.Type.INSERT)
                    .orig(CommandField.Orig.ENTITY)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Object execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.INSERT);
        return this.persistExecutor.execute(commandContext);
    }
}
