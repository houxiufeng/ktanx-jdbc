package com.ktanx.jdbc.command;

import com.ktanx.common.bean.BeanKit;
import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pager;
import com.ktanx.jdbc.management.CommandTable;
import com.ktanx.jdbc.mapping.MappingHandler;
import com.ktanx.jdbc.page.PageHandler;
import com.ktanx.jdbc.persist.PersistExecutor;

import java.util.List;

/**
 * Created by liyd on 17/4/19.
 */
public abstract class AbstractCommandExecutor {

    protected Class<?> type;

    protected CommandContextBuilder commandContextBuilder;

    protected PersistExecutor persistExecutor;

    protected PageHandler pageHandler;

    protected String dialect;

    protected MappingHandler mappingHandler;

    protected CommandTable commandTable;

    public AbstractCommandExecutor() {
        commandTable = new CommandTable();
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
        this.commandTable.setMappingHandler(mappingHandler);
    }

    public void setType(Class<?> type) {
        this.type = type;
        commandTable.setType(type);
        commandTable.setResultType(type);
    }

    public void setCommandContextBuilder(CommandContextBuilder commandContextBuilder) {
        this.commandContextBuilder = commandContextBuilder;
    }

    public void setPersistExecutor(PersistExecutor persistExecutor) {
        this.persistExecutor = persistExecutor;
    }

    public void setPageHandler(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    protected PageList<?> doPageList(int pageNum, int pageSize, CommandContext.CommandType commandType) {
        CommandContext commandContext = this.commandContextBuilder.build(this.commandTable);

        String countCommand = this.pageHandler.getCountCommand(commandContext.getCommand(), this.dialect);

        CommandContext countCommandContext = BeanKit.convert(new CommandContext(), commandContext);
        countCommandContext.setCommand(countCommand);
        countCommandContext.setCommandType(CommandContext.CommandType.SELECT_OBJECT);
        long count = (Long) this.persistExecutor.execute(countCommandContext);

        Pager pager = new Pager();
        pager.setPageSize(pageSize);
        pager.setPageNum(pageNum);
        pager.setTotalItems((int) count);

        String pageCommand = this.pageHandler.getPageCommand(commandContext.getCommand(), pager, dialect);
        CommandContext pageCommandContext = BeanKit.convert(new CommandContext(), commandContext);
        pageCommandContext.setCommand(pageCommand);
        pageCommandContext.setCommandType(commandType);
        List<?> list = (List<?>) this.persistExecutor.execute(pageCommandContext);

        PageList pageList = new PageList();
        if (list != null) {
            pageList.addAll(list);
        }
        pageList.setPager(pager);

        return pageList;
    }

}
