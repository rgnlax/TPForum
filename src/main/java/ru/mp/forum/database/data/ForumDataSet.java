package ru.mp.forum.database.data;

import java.sql.ResultSet;
import java.util.Objects;

/**
 * Created by maksim on 08.01.16.
 */
public class ForumDataSet {
    private final String shortName;
    private int id;
    private Object user;
    private final String name;

    public ForumDataSet(String shortName, int id, Object user, String name) {
        this.shortName = shortName;
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public ForumDataSet(ResultSet resultSet) throws Exception {
        this (
                resultSet.getString("short_name"),
                resultSet.getInt("id"),
                resultSet.getString("user_email"),
                resultSet.getString("name")
        );
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_name() {
        return shortName;
    }

    public int getId() {
        return id;
    }

    public Object getUser() {
        return user;
    }

    public String getName() {
        return name;
    }
}
