package com.ktanx.jdbc.persist;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.exception.KtanxJdbcException;

/**
 * Created by liyd on 17/4/12.
 */
public abstract class AbstractPersistExecutor implements PersistExecutor {

    public Object execute(CommandContext commandContext) {

        switch (commandContext.getCommandType()) {
            case INSERT:
                return this.insert(commandContext);
            case SELECT_OBJECT:
                return this.selectObject(commandContext);
            case SELECT_OBJECT_LIST:
                return this.selectObjectList(commandContext);
            case SELECT_LIST:
                return this.selectList(commandContext);
            case SELECT_SINGLE:
                return this.selectSingle(commandContext);
            case UPDATE:
                return this.update(commandContext);
            case DELETE:
                return this.delete(commandContext);
            case EXECUTE:
                return this.doExecute(commandContext);
            case SELECT_SINGLE_COLUMN:
                return this.selectSingleColumn(commandContext);
            case SELECT_SINGLE_COLUMN_LIST:
                return this.selectSingleColumnList(commandContext);
            default:
                throw new KtanxJdbcException("不支持的执行类型");
        }
    }


    public abstract Object insert(CommandContext commandContext);

    public abstract Object selectObject(CommandContext commandContext);

    public abstract Object selectObjectList(CommandContext commandContext);

    public abstract Object selectSingleColumn(CommandContext commandContext);

    public abstract Object selectSingleColumnList(CommandContext commandContext);

    public abstract Object selectList(CommandContext commandContext);

    public abstract Object selectSingle(CommandContext commandContext);

    public abstract Object update(CommandContext commandContext);

    public abstract Object delete(CommandContext commandContext);

    public abstract Object doExecute(CommandContext commandContext);
}
