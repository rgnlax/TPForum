package ru.mp.forum.database.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.UserDataSet;
import ru.mp.forum.database.executor.TExecutor;

import java.sql.*;
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
    public ReplyTuple create(String data) {
        UserDataSet user;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String username = object.get("username").getAsString();
            String email = object.get("email").getAsString();
            String name = object.get("name").getAsString();
            String about = object.get("about").getAsString();
            boolean isAnonymous = !object.has("isAnonymous") ? false : object.get("isAnonymous").getAsBoolean();

            user = new UserDataSet(email, 0 ,username, name, about, isAnonymous);

            String query = "INSERT INTO " + tableName + " (username, about, name, email, isAnonymous) VALUES (?,?,?,?,?)";

            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getAbout());
                stmt.setString(3, user.getName());
                stmt.setString(4, user.getEmail());
                stmt.setBoolean(5, user.getIsAnonymous());

                stmt.executeUpdate();

                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    resultSet.next();
                    user.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    return new ReplyTuple(Status.ALREADY_EXIST);
                }
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }

        return new ReplyTuple(Status.OK, user);
    }

    @Override
    public ReplyTuple details(String email) {
        UserDataSet user;
        String query = "SELECT * FROM User as u\n" +
                "LEFT JOIN (SELECT user_email, group_concat(followee_email) as following FROM User_followers WHERE user_email = ?) t ON u.email = t.user_email\n" +
                "LEFT JOIN (SELECT followee_email, group_concat(user_email) as followers FROM User_followers WHERE followee_email = ?) s ON u.email = s.followee_email\n" +
                "WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, email);
            stmt.setString(3, email);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();

                user = new UserDataSet(
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

            } catch (Exception e) {
                return new ReplyTuple(Status.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ReplyTuple(Status.INCORRECT_REQUEST);
        }
        return new ReplyTuple(Status.OK, user);
    }

    @Override
    public ReplyTuple follow(String data) {
        String follower;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();
            follower = object.get("follower").getAsString();
            String followee = object.get("followee").getAsString();

            try {
                String query = "INSERT INTO User_followers (user_email, followee_email) VALUES (?,?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, follower);
                    preparedStatement.setString(2, followee);
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new ReplyTuple(Status.ALREADY_EXIST);

            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(follower).getObject());
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
