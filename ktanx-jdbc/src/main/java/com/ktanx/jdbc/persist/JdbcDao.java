package com.ktanx.jdbc.persist;

import com.ktanx.common.model.PageList;
import com.ktanx.common.model.Pageable;
import com.ktanx.jdbc.command.batis.BatisExecutor;
import com.ktanx.jdbc.command.entity.Delete;
import com.ktanx.jdbc.command.entity.Insert;
import com.ktanx.jdbc.command.entity.Select;
import com.ktanx.jdbc.command.entity.Update;
import com.ktanx.jdbc.command.natives.NativeExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcDao {

    /**
     * 获取一个实体对象
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    <T> T get(Class<T> entityClass, Serializable id);

    /**
     * 查询所有列表
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> List<T> queryAll(Class<T> entityClass);

    /**
     * 根据实体条件查询列表,根据默认主键desc排序
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> queryList(T entity);

    /**
     * 查询分页列表
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends Pageable> PageList<T> queryPageList(T entity);

    /**
     * 根据实体条件查询记录数
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> long queryCount(T entity);

    /**
     * 根据实体条件查询单个结果
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T querySingleResult(T entity);

    /**
     * 插入
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> Object insert(T entity);

    /**
     * 删除
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    <T> int delete(Class<T> entityClass, Serializable id);

    /**
     * 根据实体条件删除
     *
     * @param <T>    the type parameter
     * @param entity the entity
     * @return int
     */
    <T> int delete(T entity);

    /**
     * 更新
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> int update(T entity);


    /**
     * 创建select对象
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> Select<T> createSelect(Class<T> entityClass);


    /**
     * 创建insert对象
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> Insert<T> createInsert(Class<T> entityClass);

    /**
     * 创建delete对象
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> Delete<T> createDelete(Class<T> entityClass);

    /**
     * 创建update对象
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> Update<T> createUpdate(Class<T> entityClass);

    /**
     * 创建native executor对象
     *
     * @return
     */
    NativeExecutor createNativeExecutor();

    /**
     * 创建batis executor对象
     *
     * @return
     */
    BatisExecutor createBatisExecutor();
}
