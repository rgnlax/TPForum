package ru.mp.forum.database.data;

import java.sql.ResultSet;

/**
 * Created by maksim on 08.01.16.
 */
public class ThreadDataSet {
    private int id;
    private final String title;
    private final String date;
    private final String message;
    private final String slug;
    private Object forum;
    private Object user;
    private final int points;
    private final int likes;
    private final int dislikes;
    private final boolean isDeleted;
    private final boolean isClosed;

    public ThreadDataSet(int id, String title, String date, String message, String slug, String forum, String user, int points, int likes, int dislikes, boolean isDeleted, boolean isClosed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.message = message;
        this.slug = slug;
        this.forum = forum;
        this.user = user;
        this.points = points;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
    }

    public ThreadDataSet(ResultSet resultSet) throws Exception {
        this (
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("date"),
                resultSet.getString("message"),
                resultSet.getString("slug"),
                resultSet.getString("forum_short_name"),
                resultSet.getString("user_email"),
                resultSet.getInt("points"),
                resultSet.getInt("likes"),
                resultSet.getInt("dislikes"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getBoolean("isClosed")
        );
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSlug() {
        return slug;
    }

    public Object getForum() {
        return forum;
    }

    public Object getUser() {
        return user;
    }

    public int getPoints() {
        return points;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setForum(Object forum) {
        this.forum = forum;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}
