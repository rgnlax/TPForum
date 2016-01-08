package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.PostDAO;
import ru.mp.forum.database.data.PostDataSet;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDAOImpl implements PostDAO {
    private final Connection connection;

    public PostDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void truncateTable() {

    }

    @Override
    public int getCount(int threadId) {
        return 0;
    }

    @Override
    public PostDataSet create(String jsonString) {
        return null;
    }

    @Override
    public PostDataSet details(int postId, String[] related) {
        return null;
    }

    @Override
    public ArrayList<PostDataSet> listForumPosts(String forum, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ArrayList<PostDataSet> listThreadPosts(int threadId, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public String remove(String jsonString) {
        return null;
    }

    @Override
    public String restore(String jsonString) {
        return null;
    }

    @Override
    public PostDataSet update(String jsonString) {
        return null;
    }

    @Override
    public String vote(String jsonString) {
        return null;
    }
}
