package ru.mp.forum.database.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.ForumDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.ForumDataSet;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.data.UserDataSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maksim on 08.01.16.
 */
public class ForumDAOImpl extends BaseDAOImpl implements ForumDAO {

    public ForumDAOImpl(Connection connection) {
        this.tableName = "Forum";
        this.connection = connection;
    }

    @Override
    public ReplyTuple create(String jsonString) {
        ForumDataSet forum;
        try {
            JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();

            String email = object.get("user").getAsString();
            String slug = object.get("short_name").getAsString();
            String name = object.get("name").getAsString();

            forum = new ForumDataSet(slug, 0, email, name);

            String query = "INSERT INTO Forum (name, short_name, user_email) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, forum.getName());
                stmt.setString(2, forum.getShort_name());
                stmt.setString(3, forum.getUser().toString());

                stmt.executeUpdate();

                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    resultSet.next();
                    forum.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, forum);
    }

    @Override
    public ReplyTuple details(String slug, String[] related) {
        ForumDataSet forum;
        try {
            String query = "SELECT * FROM Forum WHERE short_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, slug);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    resultSet.next();

                    forum = new ForumDataSet(resultSet);
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }

        if (related != null) {
            if (Arrays.asList(related).contains("user")) {
                forum.setUser(new UserDAOImpl(connection).details(forum.getUser().toString()).getObject());
            }
        }
        return new ReplyTuple(Status.OK, forum);
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
