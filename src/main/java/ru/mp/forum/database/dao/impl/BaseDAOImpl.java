package ru.mp.forum.database.dao.impl;

import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.BaseDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.executor.TExecutor;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.Temporal;

/**
 * Created by maksim on 09.01.16.
 */
public abstract class BaseDAOImpl implements BaseDAO {
    protected String tableName = "";
    protected Connection connection;

    @Override
    public int getCount() {
        try {
            String query = "SELECT COUNT(*) FROM " + tableName;
            return TExecutor.execQuery(connection, query, resultSet -> {
                resultSet.next();
                return resultSet.getInt(1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void truncateTable() {
        try {
            TExecutor.execQuery(connection, "SET FOREIGN_KEY_CHECKS = 0;");
            TExecutor.execQuery(connection, "TRUNCATE TABLE " + tableName);
            TExecutor.execQuery(connection, "TRUNCATE TABLE User_followers");
            TExecutor.execQuery(connection, "SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected ReplyTuple handeSQLException(SQLException e) {
        if (e.getErrorCode() == 1062) {
            return new ReplyTuple(Status.ALREADY_EXIST);
        } else {
            return new ReplyTuple(Status.NOT_FOUND);
        }
    }
}
