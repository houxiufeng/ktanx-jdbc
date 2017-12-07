package com.ktanx.jdbc.persist;

import com.ktanx.common.utils.NameUtils;

/**
 * Created by liyd on 16/8/25.
 */
public class OracleKeyGenerator implements KeyGenerator {

    public boolean isPkValueByDb() {
        return true;
    }

    public Object generateKeyValue(Class<?> clazz) {
        //根据实体名获取主键序列名
        String tableName = NameUtils.getUnderlineName(clazz.getSimpleName());
        return String.format("SEQ_%s.NEXTVAL", tableName);
    }
}
