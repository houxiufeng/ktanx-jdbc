package com.ktanx.jdbc.command.batis.xml;

import com.ktanx.jdbc.command.batis.build.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liyd on 2015-11-30.
 */
public class SetSqlNode extends TrimSqlNode {

    private static List<String> suffixList = Arrays.asList(",");

    public SetSqlNode(Configuration configuration, SqlNode contents) {
        super(configuration, contents, "SET", null, null, suffixList);
    }

}
