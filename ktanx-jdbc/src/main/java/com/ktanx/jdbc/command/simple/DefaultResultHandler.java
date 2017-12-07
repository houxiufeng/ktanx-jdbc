package com.ktanx.jdbc.command.simple;

import com.ktanx.common.bean.BeanKit;

import java.util.Map;

public class DefaultResultHandler<E> implements ResultHandler<E> {

    private Class<E> clazz;

    private DefaultResultHandler(Class<E> mappedClass) {
        this.clazz = mappedClass;
    }

    @Override
    public E handle(Object object) {
        if (object instanceof Map) {
            return (E) BeanKit.mapToBean(((Map) object), clazz, '_');
        } else {
            return (E) object;
        }
    }

    public static <E> ResultHandler<E> newInstance(Class<E> mappedClass) {
        return new DefaultResultHandler<E>(mappedClass);
    }

}
