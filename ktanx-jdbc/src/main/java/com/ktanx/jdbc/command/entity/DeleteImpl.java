package com.ktanx.jdbc.command.entity;

import com.ktanx.jdbc.command.CommandContext;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteImpl<T> extends AbstractConditionBuilder<Delete<T>> implements Delete<T> {

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.DELETE);
        return (Integer) this.persistExecutor.execute(commandContext);
    }
}
