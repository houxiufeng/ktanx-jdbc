package com.ktanx.jdbc.command.batis;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.command.batis.build.BatisContext;
import com.ktanx.jdbc.command.batis.build.BoundSql;
import com.ktanx.jdbc.command.entity.AbstractCommandContextBuilder;
import com.ktanx.jdbc.management.CommandTable;

/**
 * Created by liyd on 17/4/25.
 */
public class BatisExecutorCommandContextBuilder extends AbstractCommandContextBuilder {

    private BatisContext batisContext;

    public BatisExecutorCommandContextBuilder(BatisContext batisContext) {
        this.batisContext = batisContext;
    }

    public CommandContext doBuild(CommandTable commandTable) {
        String command = (String) commandTable.getExtendData(CommandTable.ExtendDataKey.COMMAND.name());
        Object[] parameters = (Object[]) commandTable.getExtendData(CommandTable.ExtendDataKey.PARAMETERS.name());

        BoundSql boundSql = batisContext.getBoundSql(command, parameters);

        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(boundSql.getSql());
        if (boundSql.getParameters() != null) {
            commandContext.addParameters(boundSql.getParameters());
        }
        return commandContext;
    }
}