package com.ktanx.jdbc.command.entity;

import com.ktanx.common.bean.BeanKit;
import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pageable;
import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.management.CommandField;
import com.ktanx.jdbc.management.MappingCache;

import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public class SelectImpl<T extends Object> extends AbstractConditionBuilder<Select<T>> implements Select<T> {

    public Select<T> include(String... fields) {
        this.commandTable.addIncludeFields(fields);
        return this;
    }

    public Select<T> exclude(String... fields) {
        this.commandTable.addExcludeFields(fields);
        return this;
    }

    public Select<T> addSelectField(String... fields) {
        for (String field : fields) {
            CommandField commandField = CommandField.builder()
                    .name(field)
                    .orig(CommandField.Orig.MANUAL)
                    .type(CommandField.Type.CUSTOM_SELECT_FIELD)
                    .build();
            this.commandTable.addOperationField(commandField);
        }
        return this;
    }

    public Select<T> notSelectEntityField() {
        this.commandTable.setNotSelectEntityField(true);
        return this;
    }

    public Select<T> orderBy(String... fields) {
        for (String field : fields) {
            CommandField commandField = CommandField.builder()
                    .name(field)
                    .orig(CommandField.Orig.MANUAL)
                    .type(CommandField.Type.ORDER_BY)
                    .build();
            this.commandTable.addOrderByField(commandField);
        }
        return this;
    }

    public Select<T> orderById() {
        String pkField = MappingCache.getPkField(this.commandTable);
        CommandField commandField = CommandField.builder()
                .name(pkField)
                .orig(CommandField.Orig.MANUAL)
                .type(CommandField.Type.ORDER_BY)
                .build();
        this.commandTable.addOrderByField(commandField);
        return this;
    }

    public Select<T> asc() {
        this.commandTable.setOrderBy("asc");
        return this;
    }

    public Select<T> desc() {
        this.commandTable.setOrderBy("desc");
        return this;
    }

    public long count() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        String countCommand = this.pageHandler.getCountCommand(commandContext.getCommand(), this.dialect);
        CommandContext countCommandContext = BeanKit.convert(new CommandContext(), commandContext);
        countCommandContext.setCommand(countCommand);
        countCommandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT);
        return (Long) this.persistExecutor.execute(countCommandContext);
    }

    public Object objectResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT);
        return this.persistExecutor.execute(commandContext);
    }

    public List<?> objectList() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT_LIST);
        return (List<?>) this.persistExecutor.execute(commandContext);
    }

    @Override
    public PageList<?> objectPageList(Pageable pageable) {
        return this.objectPageList(pageable.getPageNum(), pageable.getPageSize());
    }

    @Override
    public PageList<?> objectPageList(int pageNum, int pageSize) {
        return this.doPageList(pageNum, pageSize, CommandContext.CommandType.SELECT_OBJECT_LIST);
    }

    @Override
    public <E> E singleColumnResult(Class<E> clazz) {
        this.commandTable.setResultType(clazz);
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_SINGLE_COLUMN);
        return (E) this.persistExecutor.execute(commandContext);
    }

    @Override
    public <E> List<E> singleColumnList(Class<E> clazz) {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setResultType(clazz);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_SINGLE_COLUMN_LIST);
        return (List<E>) this.persistExecutor.execute(commandContext);
    }

    @Override
    public <E> PageList<E> singleColumnPageList(Class<E> clazz, Pageable pageable) {
        return this.singleColumnPageList(clazz, pageable.getPageNum(), pageable.getPageSize());
    }

    @Override
    public <E> PageList<E> singleColumnPageList(Class<E> clazz, int pageNum, int pageSize) {
        this.commandTable.setResultType(clazz);
        return (PageList<E>) this.doPageList(pageNum, pageSize, CommandContext.CommandType.SELECT_SINGLE_COLUMN_LIST);
    }

    @SuppressWarnings("unchecked")
    public T singleResult() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_SINGLE);
        return (T) this.persistExecutor.execute(commandContext);
    }

    @SuppressWarnings("unchecked")
    public List<T> list() {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);
        commandContext.setCommandType(CommandContext.CommandType.SELECT_LIST);
        return (List<T>) this.persistExecutor.execute(commandContext);
    }

    public PageList<T> pageList(Pageable pageable) {
        return this.pageList(pageable.getPageNum(), pageable.getPageSize());
    }

    @SuppressWarnings("unchecked")
    public PageList<T> pageList(int pageNum, int pageSize) {
        return (PageList<T>) this.doPageList(pageNum, pageSize, CommandContext.CommandType.SELECT_LIST);
    }

}
