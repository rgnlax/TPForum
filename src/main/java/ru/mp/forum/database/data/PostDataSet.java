package ru.mp.forum.database.data;

import com.google.gson.JsonObject;

import java.sql.ResultSet;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDataSet {
    private int id;
    private String date;
    private Object thread;
    private Object forum;
    private Object user;
    private final String message;
    private Integer parent;
    private final boolean isEdited;
    private final boolean isApproved;
    private final boolean isHighlighted;
    private final boolean isSpam;
    private final boolean isDeleted;
    private final int likes;
    private final int dislikes;

    public PostDataSet(int id, String date, int thread, String forum, String user, String message, Integer parent, boolean isEdited, boolean isApproved, boolean isHighlighted, boolean isSpam, boolean isDeleted, int likes, int dislikes) {
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
    }

    public PostDataSet(ResultSet resultSet) throws Exception {
        this (
                resultSet.getInt("id"),
                resultSet.getString("date").substring(0,19),
                resultSet.getInt("thread_id"),
                resultSet.getString("forum_short_name"),
                resultSet.getString("user_email"),
                resultSet.getString("message"),
                resultSet.getInt("m_path"),
                resultSet.getBoolean("isEdited"),
                resultSet.getBoolean("isApproved"),
                resultSet.getBoolean("isHighlighted"),
                resultSet.getBoolean("isSpam"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getInt("likes"),
                resultSet.getInt("dislikes")
        );
        //Kostyl
        if (parent == 0) {
            parent = null;
        }
    }

    public PostDataSet(JsonObject object) throws Exception {
        forum = object.get("forum").getAsString();
        thread = object.get("thread").getAsInt();
        user = object.get("user").getAsString();
        message = object.get("message").getAsString();
        date = object.get("date").getAsString();
        if (object.has("parent")) {
            if (object.get("parent").isJsonNull()) {
                parent = null;
            } else {
                parent = object.get("parent").getAsInt();
            }
        } else {
            parent = null;
        }
        isApproved = object.has("isApproved") ? object.get("isApproved").getAsBoolean() : false;
        isEdited = object.has("isEdited") ? object.get("isEdited").getAsBoolean() : false;
        isHighlighted = object.has("isHighlighted") ? object.get("isHighlighted").getAsBoolean() : false;
        isSpam = object.has("isSpam") ? object.get("isSpam").getAsBoolean() : false;
        isDeleted = object.has("isDeleted") ? object.get("isDeleted").getAsBoolean() : false;
        likes = object.has("likes") ? object.get("likes").getAsInt() : 0;
        dislikes = object.has("dislikes") ? object.get("dislikes").getAsInt() : 0;
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

    public Integer getParent() {
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

    public int getPoints() { return likes - dislikes; }

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

    public void setDate(String date) {
        this.date = date;
    }
}
