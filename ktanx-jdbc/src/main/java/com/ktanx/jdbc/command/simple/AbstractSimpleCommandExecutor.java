package com.ktanx.jdbc.command.simple;

import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pageable;
import com.ktanx.jdbc.command.AbstractCommandExecutor;
import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.management.CommandTable;

import java.util.List;

/**
 * Created by liyd on 17/4/25.
 */
public abstract class AbstractSimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends AbstractCommandExecutor implements SimpleCommandExecutor<T> {

    protected String command;

    protected Object[] parameters;

    protected ResultHandler<?> resultHandler;

    @SuppressWarnings("unchecked")
    public T command(String command) {
        this.command = command;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T parameters(Object[] values) {
        this.parameters = values;
        return (T) this;
    }

    @Override
    public <E> T resultClass(Class<E> clazz) {
        this.resultHandler = DefaultResultHandler.newInstance(clazz);
        return (T) this;
    }

    @Override
    public <E> T resultHandler(ResultHandler<E> resultHandler) {
        this.resultHandler = resultHandler;
        return (T) this;
    }

    public long count() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT);
        return (Long) this.persistExecutor.execute(commandContext);
    }

    public Object singleResult() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT);
        Object result = this.persistExecutor.execute(commandContext);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    public List<?> list() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT_LIST);
        List<?> result = (List<?>) this.persistExecutor.execute(commandContext);
        if (resultHandler != null) {
            return this.handleResult(result, resultHandler);
        }
        return result;
    }

    public PageList<?> pageList(Pageable pageable) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize());
    }

    public PageList<? extends Object> pageList(int pageNum, int pageSize) {
        this.setNativeData();
        PageList<?> pageList = this.doPageList(pageNum, pageSize, CommandContext.CommandType.SELECT_OBJECT_LIST);
        if (this.resultHandler != null) {
            return this.handleResult(pageList, resultHandler);
        }
        return pageList;
    }

    public int update() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.UPDATE);
        return (Integer) this.persistExecutor.execute(commandContext);
    }

    public void execute() {
        this.setNativeData();
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.EXECUTE);
        this.persistExecutor.execute(commandContext);
    }

    protected void setNativeData() {
        this.commandTable.addExtendData(CommandTable.ExtendDataKey.COMMAND.name(), command);
        this.commandTable.addExtendData(CommandTable.ExtendDataKey.PARAMETERS.name(), parameters);
    }
}
