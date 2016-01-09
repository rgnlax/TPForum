package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.BaseDAO;
import ru.mp.forum.database.executor.TExecutor;
import java.sql.Connection;

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
            String query = "TRUNCATE TABLE " + tableName;
            TExecutor.execQuery(connection, "SET FOREIGN_KEY_CHECKS = 0;");
            TExecutor.execQuery(connection, query);
            TExecutor.execQuery(connection, "SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
