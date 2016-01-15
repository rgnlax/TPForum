package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maksim on 09.01.16.
 */
public class MainDAOImpl implements MainDAO {
    private ForumDAO forumDAO;
    private UserDAO userDAO;
    private PostDAO postDAO;
    private ThreadDAO threadDAO;

    public MainDAOImpl(DataSource dataSource) {
        userDAO = new UserDAOImpl(dataSource);
        forumDAO = new ForumDAOImpl(dataSource);
        threadDAO = new ThreadDAOImpl(dataSource);
        postDAO = new PostDAOImpl(dataSource);
    }

    @Override
    public Map<String, Integer> getCount() {
        Map<String, Integer> response = new HashMap<>();
        response.put("user",userDAO.getCount());
        response.put("thread", threadDAO.getCount());
        response.put("forum", forumDAO.getCount());
        response.put("post", postDAO.getCount());

        return response;
    }

    @Override
    public void truncateAll() {
        userDAO.truncateTable();
        threadDAO.truncateTable();
        forumDAO.truncateTable();
        postDAO.truncateTable();
    }
}
