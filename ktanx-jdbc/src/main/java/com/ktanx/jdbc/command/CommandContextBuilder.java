package com.ktanx.jdbc.command;

import com.ktanx.jdbc.management.CommandTable;

/**
 * CommandContext构建
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface CommandContextBuilder {

    /**
     * 构建执行内容
     *
     * @param commandTable the command table
     * @return command context
     */
    CommandContext build(CommandTable commandTable);

}
