package com.ktanx.jdbc.persist;

import com.ktanx.jdbc.command.CommandContext;

/**
 * 持久化执行
 * <p>
 * Created by liyd on 17/4/11.
 */
public interface PersistExecutor {

    /**
     * 执行持久化
     *
     * @param commandContext
     * @return
     */
    Object execute(CommandContext commandContext);

}
