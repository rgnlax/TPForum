package ru.mp.forum.database.data;

import com.google.gson.JsonObject;

import java.sql.ResultSet;

/**
 * Created by maksim on 08.01.16.
 */
public class ThreadDataSet {
    private int id;
    private final String title;
    private String date;
    private final String message;
    private final String slug;
    private Object forum;
    private Object user;
    private final int likes;
    private final int dislikes;
    private final boolean isDeleted;
    private final boolean isClosed;
    private int posts;

    public ThreadDataSet(int id, String title, String date, String message, String slug, String forum, String user, int likes, int dislikes, boolean isDeleted, boolean isClosed, int posts) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.message = message;
        this.slug = slug;
        this.forum = forum;
        this.user = user;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
        this.posts = posts;
    }

    public ThreadDataSet(ResultSet resultSet) throws Exception {
        this (
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("date").substring(0,19),
                resultSet.getString("message"),
                resultSet.getString("slug"),
                resultSet.getString("forum_short_name"),
                resultSet.getString("user_email"),
                resultSet.getInt("likes"),
                resultSet.getInt("dislikes"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getBoolean("isClosed"),
                resultSet.getInt("posts")
        );
    }

    public ThreadDataSet(JsonObject object) throws Exception {
        forum = object.get("forum").getAsString();
        title = object.get("title").getAsString();
        user = object.get("user").getAsString();
        date = object.get("date").getAsString();
        message = object.get("message").getAsString();
        slug = object.get("slug").getAsString();
        isClosed = object.get("isClosed").getAsBoolean();
        isDeleted = object.has("getIsDeleted") ? object.get("getIsDeleted").getAsBoolean() : false;
        likes = object.has("likes") ? object.get("likes").getAsInt() : 0;
        dislikes = object.has("dislikes") ? object.get("dislikes").getAsInt() : 0;
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
        return likes - dislikes;
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

    public void setDate(String date) {
        this.date = date;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }
}
