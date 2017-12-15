package com.ktanx.jdbc.command.entity;

import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pageable;

import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public interface Select<T extends Object> extends ConditionBuilder<Select<T>> {

    /**
     * 白名单
     *
     * @param fields
     * @return
     */
    Select<T> include(String... fields);

    /**
     * 黑名单
     *
     * @param fields
     * @return
     */
    Select<T> exclude(String... fields);

    /**
     * 添加查询字段
     *
     * @param fields
     * @return
     */
    Select<T> addSelectField(String... fields);

    /**
     * 不查询实体类属性
     *
     * @return
     */
    Select<T> notSelectEntityField();

    /**
     * 添加 group by属性
     *
     * @param fields
     * @return
     */
    Select<T> groupBy(String... fields);

    /**
     * 排序属性
     *
     * @param fields
     * @return
     */
    Select<T> orderBy(String... fields);

    /**
     * 主键排序
     *
     * @return
     */
    Select<T> orderById();

    /**
     * asc排序
     *
     * @return
     */
    Select<T> asc();

    /**
     * desc 排序
     *
     * @return
     */
    Select<T> desc();

    /**
     * 强制处理结果成泛型实体模型
     * 仅对objectResult、objectList、objectPageList方法结果有效
     * 调用该方法后将对返回的Object类型(一般为Map<String,Object>)强制转换成泛型实体，
     * 泛型实体不包含的属性如果继承了{@link com.ktanx.common.model.Model}将放入ExtensionProperty，否则将丢失
     *
     * @return
     */
    Select<T> forceResultToModel();

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
    T singleResult();

    /**
     * 列表查询
     *
     * @return
     */
    List<T> list();

    /**
     * 分页列表查询
     *
     * @param pageable
     * @return
     */
    PageList<T> pageList(Pageable pageable);

    /**
     * 分页列表查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageList<T> pageList(int pageNum, int pageSize);

    /**
     * 查询结果，返回结果会有多个值，多数情况下为Map<column,value>
     *
     * @return
     */
    Object objectResult();

    /**
     * 查询结果，objectResult方法的列表查询，返回结果多数情况下为List<Map<column,value>>
     *
     * @return
     */
    List<?> objectList();

    /**
     * objectList分页查询
     *
     * @param pageable
     * @return
     */
    PageList<?> objectPageList(Pageable pageable);

    /**
     * objectList分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageList<?> objectPageList(int pageNum, int pageSize);

    /**
     * 简单查询，返回单一的结果，例如Long、Integer、String等
     *
     * @return
     */
    <E> E singleColumnResult(Class<E> clazz);

    /**
     * 查询结果，返回单一的结果列表，例如List<Long>
     *
     * @return
     */
    <E> List<E> singleColumnList(Class<E> clazz);


    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageable the pageable
     * @return page list
     */
    <E> PageList<E> singleColumnPageList(Class<E> clazz, Pageable pageable);

    /**
     * singleColumnList分页查询
     *
     * @param <E>      the type parameter
     * @param clazz    the clazz
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return page list
     */
    <E> PageList<E> singleColumnPageList(Class<E> clazz, int pageNum, int pageSize);

}
