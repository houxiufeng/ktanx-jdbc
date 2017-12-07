package com.ktanx.jdbc.command.simple;

public interface ResultHandler<T> {

    /**
     * 处理结果
     *
     * @param object
     * @return
     */
    T handle(Object object);
}
