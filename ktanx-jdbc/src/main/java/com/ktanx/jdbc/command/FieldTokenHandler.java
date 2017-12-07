package com.ktanx.jdbc.command;

import com.ktanx.common.parser.TokenHandler;
import com.ktanx.jdbc.management.CommandTable;
import com.ktanx.jdbc.management.MappingCache;

/**
 * native中的属性转成列
 * <p>
 * Created by liyd on 17/4/14.
 */
public class FieldTokenHandler implements TokenHandler {

    private CommandTable commandTable;

    public FieldTokenHandler(CommandTable commandTable) {
        this.commandTable = commandTable;
    }

    public String handleToken(String content) {
        return MappingCache.getColumn(this.commandTable, content);
    }
}
