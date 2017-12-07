package com.ktanx.jdbc.command.batis.xml;

import com.ktanx.jdbc.command.batis.build.DynamicContext;

/**
 * Created by liyd on 2015-11-30.
 */
public class StaticTextSqlNode implements SqlNode {
    private String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    public boolean apply(DynamicContext context) {
        context.appendSql(text.trim());
        return true;
    }

}
