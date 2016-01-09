package ru.mp.forum.database.dao.impl;

import ru.mp.forum.database.dao.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maksim on 09.01.16.
 */
public class MainDAOImpl implements MainDAO {
    private final Connection con;

    private ForumDAO forumDAO;
    private UserDAO userDAO;
    private PostDAO postDAO;
    private ThreadDAO threadDAO;

    public MainDAOImpl(Connection con) {
        this.con = con;

        userDAO = new UserDAOImpl(con);
        forumDAO = new ForumDAOImpl(con);
        threadDAO = new ThreadDAOImpl(con);
        postDAO = new PostDAOImpl(con);
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
