package ru.mp.forum.database.dao;

import java.util.Map;

/**
 * Created by maksim on 09.01.16.
 */
public interface MainDAO {
    Map<String, Integer> getCount();

    void truncateAll();
}
