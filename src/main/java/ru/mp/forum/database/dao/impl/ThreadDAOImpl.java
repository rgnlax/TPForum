package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.ThreadDAO;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.executor.TExecutor;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public class ThreadDAOImpl extends BaseDAOImpl implements ThreadDAO {

    public ThreadDAOImpl(Connection connection) {
        this.tableName = "Thread";
        this.connection = connection;
    }

    @Override
    public ThreadDataSet create(String jsonString) {
        return null;
    }

    @Override
    public ThreadDataSet details(int threadId, String[] related) {
        return null;
    }

    @Override
    public ArrayList<PostDataSet> listPosts(int threadId, String since, Integer limit, String sort, String order) {
        return null;
    }

    @Override
    public ArrayList<ThreadDataSet> listUserThreads(String user, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ArrayList<ThreadDataSet> listForumThreads(String forum, String since, Integer limit, String order) {
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
    public ThreadDataSet update(String jsonString) {
        return null;
    }

    @Override
    public ThreadDataSet vote(String jsonString) {
        return null;
    }

    @Override
    public String subscribe(String jsonString) {
        return null;
    }

    @Override
    public String unsubscribe(String jsonString) {
        return null;
    }

    @Override
    public String open(String jsonString) {
        return null;
    }

    @Override
    public String close(String jsonString) {
        return null;
    }
}
