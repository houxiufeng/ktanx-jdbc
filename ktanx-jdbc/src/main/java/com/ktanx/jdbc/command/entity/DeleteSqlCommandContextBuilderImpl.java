package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.management.CommandTable;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteSqlCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "delete from ";

    public CommandContext doBuild(CommandTable commandTable) {
        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        command.append(this.getTableAliasName(commandTable));

        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
        command.append(whereCommandContext.getCommand());

        CommandContext commandContext = getGenericCommandContext(commandTable);
        commandContext.setCommand(command.toString());
        commandContext.addParameterNames(whereCommandContext.getParameterNames());
        commandContext.addParameters(whereCommandContext.getParameters());

        return commandContext;
    }
}
