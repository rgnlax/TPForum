package ru.mp.forum.database.dao;

/**
 * Created by maksim on 09.01.16.
 */
public interface BaseDAO {
    int getCount();

    void truncateTable();
}
