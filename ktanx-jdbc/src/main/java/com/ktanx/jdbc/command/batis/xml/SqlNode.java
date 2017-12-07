package com.ktanx.jdbc.command.batis.xml;


import com.ktanx.jdbc.command.batis.build.DynamicContext;

/**
 * Created by liyd on 2015-11-25.
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
