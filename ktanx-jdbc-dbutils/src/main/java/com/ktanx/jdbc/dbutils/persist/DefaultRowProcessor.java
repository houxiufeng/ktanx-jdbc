package com.ktanx.jdbc.dbutils.persist;

import com.ktanx.common.utils.ClassUtils;
import com.ktanx.common.utils.NameUtils;
import com.ktanx.jdbc.annotation.Column;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyd on 17/8/31.
 */
public class DefaultRowProcessor<T> implements JdbcRowProcessor<T> {

    private Class<T> type;

    /**
     * Map of the fields we provide mapping for
     */
    private Map<String, PropertyDescriptor> mappedFields;

    /**
     * Set of bean properties we provide mapping for
     */
//    private Set<String> mappedProperties;
    public DefaultRowProcessor(Class<T> type) {
        this.initialize(type);
    }

    /**
     * Initialize the mapping metadata for the given class.
     *
     * @param type the mapped class
     */
    protected void initialize(Class<T> type) {
        this.type = type;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
//        this.mappedProperties = new HashSet<String>();
        PropertyDescriptor[] pds = ClassUtils.getPropertyDescriptors(type);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {

                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    Column aColumn = readMethod.getAnnotation(Column.class);
                    if (aColumn != null) {
                        String name = NameUtils.getLegalName(aColumn.value());
                        this.mappedFields.put(name.toLowerCase(), pd);
                    }
                }

                this.mappedFields.put(pd.getName().toLowerCase(), pd);
                String underlineName = NameUtils.getUnderlineName(pd.getName()).toLowerCase();
                if (!pd.getName().toUpperCase().equals(underlineName)) {
                    this.mappedFields.put(underlineName, pd);
                }
//                this.mappedProperties.add(pd.getName());
            }
        }
    }


    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        T obj = (T) ClassUtils.newInstance(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int index = 1; index <= columnCount; index++) {

            String columnName = rsmd.getColumnLabel(index);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(index);
            }
            String field = columnName.replaceAll(" ", "").toLowerCase();
            PropertyDescriptor pd = this.mappedFields.get(field);
            if (pd != null) {
                Object value = getResultSetValue(rs, index, pd.getPropertyType());
                Method writeMethod = pd.getWriteMethod();
                ClassUtils.invokeMethod(writeMethod, obj, value);
            }
        }
        return obj;
    }

    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {

        Object value;
        if (String.class == requiredType) {
            return rs.getString(index);
        } else if (boolean.class == requiredType || Boolean.class == requiredType) {
            value = rs.getBoolean(index);
        } else if (byte.class == requiredType || Byte.class == requiredType) {
            value = rs.getByte(index);
        } else if (short.class == requiredType || Short.class == requiredType) {
            value = rs.getShort(index);
        } else if (int.class == requiredType || Integer.class == requiredType) {
            value = rs.getInt(index);
        } else if (long.class == requiredType || Long.class == requiredType) {
            value = rs.getLong(index);
        } else if (float.class == requiredType || Float.class == requiredType) {
            value = rs.getFloat(index);
        } else if (double.class == requiredType || Double.class == requiredType ||
                Number.class == requiredType) {
            value = rs.getDouble(index);
        } else if (BigDecimal.class == requiredType) {
            value = rs.getBigDecimal(index);
        } else if (java.sql.Date.class == requiredType) {
            value = rs.getDate(index);
        } else if (java.sql.Time.class == requiredType) {
            value = rs.getTime(index);
        } else if (java.sql.Timestamp.class == requiredType || java.util.Date.class == requiredType) {
            value = rs.getTimestamp(index);
        } else if (byte[].class == requiredType) {
            value = rs.getBytes(index);
        } else if (Blob.class == requiredType) {
            value = rs.getBlob(index);
        } else if (Clob.class == requiredType) {
            value = rs.getClob(index);
        } else {
            value = rs.getObject(index, requiredType);
        }
        return (rs.wasNull() ? null : value);
    }

}
