package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.BaseDAO;
import ru.mp.forum.database.dao.ForumDAO;
import ru.mp.forum.database.data.ForumDataSet;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.data.UserDataSet;
import ru.mp.forum.database.executor.TExecutor;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public class ForumDAOImpl extends BaseDAOImpl implements ForumDAO {

    public ForumDAOImpl(Connection connection) {
        this.tableName = "Forum";
        this.connection = connection;
    }

    @Override
    public ForumDataSet create(String jsonString) {
        return null;
    }

    @Override
    public ForumDataSet details(String forum, String[] related) {
        return null;
    }

    @Override
    public ArrayList<PostDataSet> listPosts(String forum, String since, Integer limit, String order, String[] related) {
        return null;
    }

    @Override
    public ArrayList<ThreadDataSet> listThreads(String forum, String since, Integer limit, String order, String[] related) {
        return null;
    }

    @Override
    public ArrayList<UserDataSet> listUsers(String forum, Integer sinceId, Integer limit, String order) {
        return null;
    }
}
