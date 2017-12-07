package com.ktanx.jdbc.dbutils.persist;

import com.ktanx.jdbc.command.CommandContext;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import com.ktanx.jdbc.persist.AbstractPersistExecutor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by liyd on 17/8/31.
 */
public class DbUtilsPersistExecutor extends AbstractPersistExecutor {

    private DataSource dataSource;


    public Object insert(final CommandContext commandContext) {

        QueryRunner queryRunner = new QueryRunner(dataSource);
        Object id;
        try {
            id = queryRunner.insert(commandContext.getCommand(), new ScalarHandler(), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
        final String pkColumn = commandContext.getPkColumn();
        //数据库生成 或没有设置主键值 处理
        if (commandContext.isPkValueByDb() || !commandContext.getParameterNames().contains(pkColumn)) {
            return id;
        } else {
            return commandContext.getKeyGeneratorValue();
        }
    }

    public Object selectObject(CommandContext commandContext) {

        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            Map<String, Object> map = queryRunner.query(commandContext.getCommand(), new MapHandler(), commandContext.getParameters().toArray());
            if (map != null && map.size() == 1) {
                return map.values().iterator().next();
            }
            return map;
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    public Object selectObjectList(CommandContext commandContext) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(commandContext.getCommand(), new MapListHandler(), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    @Override
    public Object selectSingleColumn(CommandContext commandContext) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            Object obj = queryRunner.query(commandContext.getCommand(), new ScalarHandler(), commandContext.getParameters().toArray());
            return obj;
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    @Override
    public Object selectSingleColumnList(CommandContext commandContext) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(commandContext.getCommand(), new ColumnListHandler(), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    public Object selectList(CommandContext commandContext) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(commandContext.getCommand(), JdbcBeanListHandler.newInstance(commandContext.getResultType()), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    public Object selectSingle(CommandContext commandContext) {

        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(commandContext.getCommand(), JdbcBeanHandler.newInstance(commandContext.getResultType()), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    public Object update(CommandContext commandContext) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.update(commandContext.getCommand(), commandContext.getParameters().toArray());
        } catch (SQLException e) {
            throw new KtanxJdbcException(e);
        }
    }

    public Object delete(CommandContext commandContext) {
        return this.update(commandContext);
    }

    public Object doExecute(CommandContext commandContext) {
        return this.update(commandContext);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
