package ru.mp.forum.database.data;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    private String[] subscriptions;

    public UserDataSet(String email, int id, String username, String name, String about, boolean isAnonymous) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.name = name;
        this.about = about;
        this.isAnonymous = isAnonymous;
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
        }
        if (following != null) {
            this.following = following.split(",");
        }
        if (subscriptions != null) {
            this.subscriptions = subscriptions.split(",");
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
                    null
            );
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

    public String[] getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(String[] subscriptions) {
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
