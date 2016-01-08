package ru.mp.forum.database.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by maksim on 08.01.16.
 */
public interface TResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
