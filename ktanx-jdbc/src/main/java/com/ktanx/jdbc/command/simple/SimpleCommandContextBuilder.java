package com.ktanx.jdbc.command.simple;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.command.entity.AbstractCommandContextBuilder;
import com.ktanx.jdbc.management.CommandTable;

import java.util.Arrays;

/**
 * Created by liyd on 17/4/25.
 */
public class SimpleCommandContextBuilder extends AbstractCommandContextBuilder {

    public CommandContext doBuild(CommandTable commandTable) {
        String command = (String) commandTable.getExtendData(CommandTable.ExtendDataKey.COMMAND.name());
        Object[] parameters = (Object[]) commandTable.getExtendData(CommandTable.ExtendDataKey.PARAMETERS.name());
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(command);
        if (parameters != null) {
            commandContext.addParameters(Arrays.asList(parameters));
        }
        return commandContext;
    }
}
