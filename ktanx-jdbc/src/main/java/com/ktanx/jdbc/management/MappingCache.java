package com.ktanx.jdbc.management;

import com.ktanx.common.utils.ClassUtils;
import com.ktanx.jdbc.annotation.Column;
import com.ktanx.jdbc.annotation.Table;
import com.ktanx.jdbc.annotation.Transient;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.mapping.MappingHandler;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by liyd on 17/4/11.
 */
public class MappingCache {

    private static final Map<Class<?>, TableMetaData> CACHE = new WeakHashMap<Class<?>, TableMetaData>();


    /**
     * 初始化实体类缓存
     *
     * @param commandTable
     */
    private static TableMetaData initCache(CommandTable commandTable) {

        TableMetaData tableMetaData = new TableMetaData();
        Class<?> entityClass = commandTable.getType();
        tableMetaData.setEntityClass(entityClass);

        MappingHandler mappingHandler = commandTable.getMappingHandler();

        Table aTable = entityClass.getAnnotation(Table.class);
        String pkField = null;
        if (aTable != null) {
            tableMetaData.setTableName(aTable.name());
            pkField = aTable.pkField();
        } else {
            //如果有分表数据，不能使用缓存
            tableMetaData.setTableName(mappingHandler.getTableName(entityClass, null));
        }
        if (StringUtils.isBlank(pkField)) {
            pkField = mappingHandler.getPkFieldName(entityClass);
        }
        tableMetaData.setPkField(pkField);

        //ClassUtils已经使用了缓存
        BeanInfo selfBeanInfo = ClassUtils.getSelfBeanInfo(entityClass);
        PropertyDescriptor[] propertyDescriptors = selfBeanInfo.getPropertyDescriptors();
        Map<String, String> classFields = new HashMap<String, String>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Transient aTransient = readMethod.getAnnotation(Transient.class);
            if (aTransient != null) {
                continue;
            }
            Column column = readMethod.getAnnotation(Column.class);
            if (column == null) {
                classFields.put(pd.getName(), mappingHandler.getColumnName(entityClass, pd.getName()));
            } else {
                classFields.put(pd.getName(), column.value());
            }
        }

        tableMetaData.setPkColumn(classFields.get(pkField));
        tableMetaData.setClassFields(classFields);
        CACHE.put(entityClass, tableMetaData);

        return tableMetaData;
    }

    public static TableMetaData getTableMetaData(CommandTable commandTable) {
        if (commandTable.getType() == null || commandTable.getMappingHandler() == null) {
            throw new KtanxJdbcException("CommandTable中的entityClass和MappingHandler不能为空");
        }
        TableMetaData tableMetaData = CACHE.get(commandTable.getType());
        if (tableMetaData == null) {
            tableMetaData = initCache(commandTable);
        }
        return tableMetaData;
    }

    /**
     * 获取表名
     *
     * @param commandTable
     * @return
     */
    public static String getTableName(CommandTable commandTable) {
        return getTableMetaData(commandTable).getTableName();
    }

    /**
     * 获取主键属性
     *
     * @param commandTable
     * @return
     */
    public static String getPkField(CommandTable commandTable) {
        return getTableMetaData(commandTable).getPkField();
    }

    /**
     * 获取主键列
     *
     * @param commandTable
     * @return
     */
    public static String getPkColumn(CommandTable commandTable) {
        return getTableMetaData(commandTable).getPkColumn();
    }

    /**
     * 根据属性获取列
     *
     * @param commandTable
     * @param field
     * @return
     */
    public static String getColumn(CommandTable commandTable, String field) {
        String column = getTableMetaData(commandTable).getClassFields().get(field);
        return column != null ? column : field;
    }

    /**
     * 获取class的属性
     *
     * @param commandTable
     * @return
     */
    public static Set<String> getClassFields(CommandTable commandTable) {
        return getTableMetaData(commandTable).getClassFields().keySet();
    }


}
