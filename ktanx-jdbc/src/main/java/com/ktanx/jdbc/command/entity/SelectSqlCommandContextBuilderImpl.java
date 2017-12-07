package com.ktanx.jdbc.command.entity;

import com.ktanx.common.parser.GenericTokenParser;
import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.command.FieldTokenHandler;
import com.ktanx.jdbc.management.CommandField;
import com.ktanx.jdbc.management.CommandTable;

import java.util.Set;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectSqlCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "select ";

    public CommandContext doBuild(CommandTable commandTable) {

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        //处理操作属性,select中手动添加的都是native的
        for (CommandField commandField : commandTable.getOperationFields()) {

            //手动添加的部分，不处理黑白名单
            GenericTokenParser fieldTokenParser = new GenericTokenParser(NATIVE_FIELD_OPEN_TOKEN, NATIVE_FIELD_CLOSE_TOKEN, new FieldTokenHandler(commandTable));
            String field = fieldTokenParser.parse(commandField.getName());
            String column = this.getTableAliasNativeField(commandTable, field);
            command.append(column).append(",");
        }

        if (!commandTable.isNotSelectEntityField()) {

            Set<String> classFields = this.getClassFields(commandTable);
            for (String classField : classFields) {

                //白名单 黑名单
                if (!commandTable.isIncludeField(classField)) {
                    continue;
                } else if (commandTable.isExcludeField(classField)) {
                    continue;
                }
                //手动添加的部分，不处理黑白名单
                String column = this.getTableAliasColumn(commandTable, classField);
                command.append(column).append(",");
            }
        }

        command.deleteCharAt(command.length() - 1);
        command.append(" from ").append(this.getTableAliasName(commandTable));

        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
        command.append(whereCommandContext.getCommand());

        String orderBySql = this.buildOrderBySql(commandTable);

        command.append(orderBySql);

        CommandContext commandContext = getGenericCommandContext(commandTable);

        commandContext.setCommand(command.toString());
        commandContext.addParameterNames(whereCommandContext.getParameterNames());
        commandContext.addParameters(whereCommandContext.getParameters());

        return commandContext;
    }
}
