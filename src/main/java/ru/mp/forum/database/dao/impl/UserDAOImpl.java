package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.UserDataSet;
import ru.mp.forum.database.executor.TExecutor;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public class UserDAOImpl extends BaseDAOImpl implements UserDAO {

    public UserDAOImpl(Connection connection) {
        this.tableName = "User";
        this.connection = connection;
    }

    @Override
    public UserDataSet create(String jsonString) {
        return null;
    }

    @Override
    public UserDataSet details(String email) {
        return null;
    }

    @Override
    public void follow(String follower, String followee) {

    }

    @Override
    public void unfollow(String follower, String followee) {

    }

    @Override
    public ArrayList<UserDataSet> listFollowers(String email, Integer limit, String order, Integer sinceId) {
        return null;
    }

    @Override
    public ArrayList<UserDataSet> listFollowing(String email, Integer limit, String order, Integer sinceId) {
        return null;
    }

    @Override
    public ArrayList<PostDataSet> listPosts(String email, Integer limit, String order, String since) {
        return null;
    }

    @Override
    public void updateProfile(String jsonString) {

    }
}
