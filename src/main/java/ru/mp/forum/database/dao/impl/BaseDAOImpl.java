package ru.mp.forum.database.dao.impl;

import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.BaseDAO;
import ru.mp.forum.database.dao.impl.reply.Reply;
import ru.mp.forum.database.executor.TExecutor;
import java.sql.Connection;
import java.sql.SQLException;

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
            if (tableName == "Thread" || tableName == "Post") {
                query += " AND isDeleted = false";
            }
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
            if (tableName == "User") {
                TExecutor.execQuery(connection, "TRUNCATE TABLE User_followers");
            }
            if (tableName == "Thread") {
                TExecutor.execQuery(connection, "TRUNCATE TABLE User_subscribes");
            }
            TExecutor.execQuery(connection, "SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected Reply handeSQLException(SQLException e) {
        if (e.getErrorCode() == 1062) {
            return new Reply(Status.ALREADY_EXIST);
        } else {
            return new Reply(Status.NOT_FOUND);
        }
    }
}
