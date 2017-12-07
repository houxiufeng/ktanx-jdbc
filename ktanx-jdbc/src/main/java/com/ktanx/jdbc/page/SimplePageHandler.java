package com.ktanx.jdbc.page;

import com.ktanx.common.model.Pager;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 16/6/8.
 */
public class SimplePageHandler implements PageHandler {

    public String getCountCommand(String sql, String dialect) {
        return new StringBuilder("select count(*) from (").append(sql).append(") tmp_count").toString();
    }

    public String getPageCommand(String sql, Pager pager, String dialect) {
        StringBuilder pageSql = new StringBuilder(200);
        if (StringUtils.indexOfIgnoreCase(dialect, "mysql") != -1) {
            pageSql.append(sql);
            pageSql.append(" limit ");
            pageSql.append(pager.getBeginIndex());
            pageSql.append(",");
            pageSql.append(pager.getPageSize());
        } else if (StringUtils.indexOfIgnoreCase(dialect, "oracle") != -1) {
            pageSql.append("select * from ( select rownum num,temp.* from (");
            pageSql.append(sql);
            pageSql.append(") temp where rownum <= ").append(pager.getEndIndex());
            pageSql.append(") where num > ").append(pager.getBeginIndex());
        }
        return pageSql.toString();
    }
}
