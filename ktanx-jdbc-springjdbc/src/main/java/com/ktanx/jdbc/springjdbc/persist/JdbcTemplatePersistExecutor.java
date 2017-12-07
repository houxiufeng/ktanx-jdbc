package com.ktanx.jdbc.springjdbc.persist;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.persist.AbstractPersistExecutor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplatePersistExecutor extends AbstractPersistExecutor {

    private JdbcOperations jdbcOperations;

    public Object insert(final CommandContext commandContext) {
        final String pkColumn = commandContext.getPkColumn();
        //数据库生成 或没有设置主键值 处理
        if (commandContext.isPkValueByDb() || !commandContext.getParameterNames().contains(pkColumn)) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(commandContext.getCommand(), new String[]{pkColumn});
                    ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(commandContext.getParameters()
                            .toArray());
                    pss.setValues(ps);
                    return ps;
                }
            }, keyHolder);
            Number number = keyHolder.getKey();
            //可能显示设置了主键值，没有生成
            return number == null ? null : number.longValue();
        } else {
            jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
            return commandContext.getKeyGeneratorValue();
        }
    }

    public Object selectObject(CommandContext commandContext) {
        Map<String, Object> map = jdbcOperations.queryForMap(commandContext.getCommand(), commandContext.getParameters().toArray());
        if (map != null && map.size() == 1) {
            return map.values().iterator().next();
        }
        return map;
    }

    public Object selectObjectList(CommandContext commandContext) {
        return jdbcOperations.queryForList(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    public Object selectSingleColumn(CommandContext commandContext) {
        return jdbcOperations.queryForObject(commandContext.getCommand(), commandContext.getParameters().toArray(), commandContext.getResultType());
    }

    public Object selectSingleColumnList(CommandContext commandContext) {
        return jdbcOperations.queryForList(commandContext.getCommand(), commandContext.getResultType(), commandContext.getParameters().toArray());
    }

    public Object selectList(CommandContext commandContext) {
        return jdbcOperations.query(commandContext.getCommand(), commandContext.getParameters().toArray(), JdbcRowMapper.newInstance(commandContext.getResultType()));
    }

    public Object selectSingle(CommandContext commandContext) {
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcOperations.query(commandContext.getCommand(), commandContext.getParameters().toArray(), JdbcRowMapper.newInstance(commandContext.getResultType()));
        return DataAccessUtils.singleResult(list);
    }

    public Object update(CommandContext commandContext) {
        return jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    public Object delete(CommandContext commandContext) {
        return jdbcOperations.update(commandContext.getCommand(), commandContext.getParameters().toArray());
    }

    public Object doExecute(CommandContext commandContext) {
        return this.update(commandContext);
    }

    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
}
