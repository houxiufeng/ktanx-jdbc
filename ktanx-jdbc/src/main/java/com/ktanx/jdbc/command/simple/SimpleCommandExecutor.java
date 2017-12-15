package com.ktanx.jdbc.command.simple;

import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pageable;
import com.ktanx.jdbc.command.CommandExecutor;

import java.util.List;

/**
 * Created by liyd on 17/4/25.
 */
public interface SimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends CommandExecutor {

    /**
     * 命令
     *
     * @param command
     * @return
     */
    T command(String command);

    /**
     * 参数
     *
     * @param parameters
     * @return
     */
    T parameters(Object[] parameters);

    /**
     * 返回结果类对象
     *
     * @param clazz
     * @param <E>
     * @return
     */
    <E> T resultClass(Class<E> clazz);

    /**
     * 结果处理器
     *
     * @param resultHandler
     * @param <E>
     * @return
     */
    <E> T resultHandler(ResultHandler<E> resultHandler);


    /**
     * count查询
     *
     * @return
     */
    long count();

    /**
     * 单个结果
     *
     * @return
     */
    Object singleResult();

    /**
     * 列表查询
     *
     * @return
     */
    List<?> list();

    /**
     * 分页列表查询
     *
     * @param pageable
     * @return
     */
    PageList<?> pageList(Pageable pageable);

    /**
     * 分页列表查询
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return page list
     */
    PageList<?> pageList(int pageNum, int pageSize);

    /**
     * 更新
     *
     * @return
     */
    int update();

    /**
     * 执行
     */
    void execute();
}
