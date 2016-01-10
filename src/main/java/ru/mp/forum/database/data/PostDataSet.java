package ru.mp.forum.database.data;

import java.sql.ResultSet;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDataSet {
    private int id;
    private final String date;
    private Object thread;
    private Object forum;
    private Object user;
    private final String message;
    private final int parent;
    private final boolean isEdited;
    private final boolean isApproved;
    private final boolean isHighlighted;
    private final boolean isSpam;
    private final boolean isDeleted;
    private final int likes;
    private final int dislikes;
    private final int points;

    public PostDataSet(int id, String date, int thread, String forum, String user, String message, int parent, boolean isEdited, boolean isApproved, boolean isHighlighted, boolean isSpam, boolean isDeleted, int likes, int dislikes, int points) {
        this.id = id;
        this.date = date;
        this.thread = thread;
        this.forum = forum;
        this.user = user;
        this.message = message;
        this.parent = parent;
        this.isEdited = isEdited;
        this.isApproved = isApproved;
        this.isHighlighted = isHighlighted;
        this.isDeleted = isDeleted;
        this.isSpam = isSpam;
        this.likes = likes;
        this.dislikes = dislikes;
        this.points = points;
    }

    public PostDataSet(ResultSet resultSet) throws Exception {
        this (
                resultSet.getInt("id"),
                resultSet.getString("date"),
                resultSet.getInt("thread_id"),
                resultSet.getString("forum_short_name"),
                resultSet.getString("user_email"),
                resultSet.getString("message"),
                resultSet.getInt("parent_id"),
                resultSet.getBoolean("isEdited"),
                resultSet.getBoolean("isApproved"),
                resultSet.getBoolean("isHighlighted"),
                resultSet.getBoolean("isSpam"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getInt("likes"),
                resultSet.getInt("dislikes"),
                resultSet.getInt("points")
        );
    }


    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Object getThread() {
        return thread;
    }

    public Object getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public int getParent() {
        return parent;
    }

    public boolean getIsEdited() {
        return isEdited;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public boolean getIsSpam() {
        return isSpam;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getPoints() { return points; }

    public Object getForum() { return forum; }

    public void setId(int id) {
        this.id = id;
    }

    public void setThread(Object thread) {
        this.thread = thread;
    }

    public void setForum(Object forum) {
        this.forum = forum;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}
