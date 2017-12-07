package com.ktanx.jdbc.command.batis.parser;

import com.ktanx.common.parser.TokenHandler;
import com.ktanx.common.utils.ClassUtils;
import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.management.CommandTable;
import com.ktanx.jdbc.management.MappingCache;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by liyd on 16/3/31.
 */
public class ClassFieldHandler implements TokenHandler {

    private JdbcEngineConfig jdbcEngineConfig;

    public ClassFieldHandler(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }

    public String handleToken(String content) {

        int start = StringUtils.indexOf(content, ")") + 1;
        int end = StringUtils.indexOf(content, "[");
        String clazzName = StringUtils.substring(content, start != -1 ? start : 0, end != -1 ? end : content.length());
        String tableAlias = this.getTokenStr(content, "(", ")");
        List<String> includeFields = this.getTokenStrSplitList(content, "[+", "+]", ",");
        List<String> excludeFields = this.getTokenStrSplitList(content, "[-", "-]", ",");
        Class<?> clazz = ClassUtils.loadClass(StringUtils.trim(clazzName));

        CommandTable commandTable = new CommandTable();
        commandTable.setType(clazz);
        commandTable.setTableAlias(tableAlias);
        commandTable.setMappingHandler(jdbcEngineConfig.getMappingHandler(clazz));

        Set<String> classFields = MappingCache.getClassFields(commandTable);

        StringBuilder sb = new StringBuilder();
        for (String field : classFields) {

            if (excludeFields.contains(field)) {
                continue;
            }
            String column = MappingCache.getColumn(commandTable, field);
            if (!includeFields.isEmpty()) {
                if (includeFields.contains(field)) {
                    sb.append(column).append(",");
                }
                continue;
            }
            if (StringUtils.isNotBlank(tableAlias)) {
                sb.append(tableAlias).append(".");
            }
            sb.append(column).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }

    private List<String> getTokenStrSplitList(String content, String openToken, String closeToken, String split) {
        String subStr = this.getTokenStr(content, openToken, closeToken);
        return Arrays.asList(StringUtils.split(subStr.replaceAll(" ", ""), split));
    }

    private String getTokenStr(String content, String openToken, String closeToken) {
        int start = StringUtils.indexOf(content, openToken);
        int end = StringUtils.indexOf(content, closeToken);
        if (start == -1 || end == -1) {
            return "";
        }
        return StringUtils.substring(content, start + openToken.length(), end);
    }
}
