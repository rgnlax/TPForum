package ru.mp.forum.database.data;

import com.google.gson.JsonObject;

import java.sql.ResultSet;

/**
 * Created by maksim on 08.01.16.
 */
public class UserDataSet {
    private final String email;
    private int id;
    private final String username;
    private final String name;
    private final String about;
    private final boolean isAnonymous;
    private String[] followers;
    private String[] following;
    private Integer[] subscriptions;

    public UserDataSet(String email, int id, String username, String name, String about, boolean isAnonymous) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.name = name;
        this.about = about;
        this.isAnonymous = isAnonymous;
        this.followers = new String[]{};
        this.following = new String[]{};
        this.subscriptions = new Integer[]{};

    }

    public UserDataSet(String email, int id, String username, String name, String about, boolean isAnonymous, String followers, String following, String subscriptions) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.name = name;
        this.about = about;
        this.isAnonymous = isAnonymous;

        if (followers != null) {
            this.followers = followers.split(",");
        } else {
            this.followers = new String[]{};
        }
        if (following != null) {
            this.following = following.split(",");
        } else {
            this.following = new String[]{};
        }
        if (subscriptions != null) {
            String[] subs = subscriptions.split(",");
            this.subscriptions = new Integer[subs.length];
            for (int i=0; i < subs.length; i++) {
                this.subscriptions[i] = Integer.parseInt(subs[i]);
            }
        } else {
            this.subscriptions = new Integer[]{};
        }
    }

    public UserDataSet(ResultSet resultSet) throws Exception {
            this (
                    resultSet.getString("email"),
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getString("about"),
                    resultSet.getBoolean("isAnonymous"),
                    resultSet.getString("followers"),
                    resultSet.getString("following"),
                    resultSet.getString("subscribes")
            );
    }

    public UserDataSet(JsonObject object) throws Exception {
        username = object.get("username").isJsonNull() ? null : object.get("username").getAsString();
        email = object.get("email").isJsonNull() ? null : object.get("email").getAsString();
        name = object.get("name").isJsonNull() ? null : object.get("name").getAsString();
        about = object.get("about").isJsonNull() ? null : object.get("about").getAsString();
        isAnonymous = !object.has("isAnonymous") ? false : object.get("isAnonymous").getAsBoolean();
        id = object.has("id") ? object.get("id").getAsInt() : 0;
    }

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }

    public Integer[] getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Integer[] subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getEmail() { return email; }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }

    public boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setId(int id) {
        this.id = id;
    }
}
