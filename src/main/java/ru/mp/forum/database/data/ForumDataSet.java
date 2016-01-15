package ru.mp.forum.database.data;

import com.google.gson.JsonObject;

import java.sql.ResultSet;

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

    public ForumDataSet(JsonObject object) throws Exception {
        user = object.get("user").getAsString();
        shortName = object.get("short_name").getAsString();
        name = object.get("name").getAsString();
        id = object.has("id") ? object.get("id").getAsInt() : 0;
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
