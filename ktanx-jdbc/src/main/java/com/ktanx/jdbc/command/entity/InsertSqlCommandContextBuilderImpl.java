package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.management.CommandField;
import com.ktanx.jdbc.management.CommandTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertSqlCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "insert into ";

    public CommandContext doBuild(CommandTable commandTable) {

        CommandContext commandContext = getGenericCommandContext(commandTable);

        if (this.keyGenerator != null) {
            String pkField = this.getPkField(commandTable);
            Object generateKeyValue = this.keyGenerator.generateKeyValue(commandTable.getType());
            commandContext.setKeyGeneratorValue(generateKeyValue);
            CommandField commandField = CommandField.builder()
                    .name(pkField)
                    .value(generateKeyValue)
                    .type(this.keyGenerator.isPkValueByDb() ? CommandField.Type.INSERT_PK_NATIVE : CommandField.Type.INSERT)
                    .orig(CommandField.Orig.GENERATOR)
                    .build();
            commandTable.addOperationField(commandField);
        }

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        StringBuilder argsCommand = new StringBuilder("(");
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        command.append(this.getTableName(commandTable)).append(" (");

        for (CommandField commandField : commandTable.getOperationFields()) {
            String column = this.getColumn(commandTable, commandField.getName());
            command.append(column).append(",");
            //数据库生成值，不能传参
            if (commandField.getType() == CommandField.Type.INSERT_PK_NATIVE) {
                argsCommand.append(commandField.getValue()).append(",");
            } else {
                argsCommand.append("?").append(",");
                paramNames.add(column);
                paramValues.add(commandField.getValue());
            }
        }
        command.deleteCharAt(command.length() - 1);
        argsCommand.deleteCharAt(argsCommand.length() - 1);
        argsCommand.append(")");
        command.append(")").append(" values ").append(argsCommand);

        commandContext.setCommand(command.toString());
        commandContext.addParameterNames(paramNames);
        commandContext.addParameters(paramValues);

        return commandContext;
    }
}
