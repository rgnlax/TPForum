package ru.mp.forum.database.dao;

import java.sql.Connection;

/**
 * Created by maksim on 09.01.16.
 */
public interface BaseDAO {
    int getCount();

    void truncateTable();
}
