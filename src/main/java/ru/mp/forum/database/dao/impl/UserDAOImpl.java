package ru.mp.forum.database.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.UserDataSet;

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

            user = new UserDataSet(email, 0, username, name, about, isAnonymous);

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
                handeSQLException(e);
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
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(follower).getObject());
    }

    @Override
    public ReplyTuple unfollow(String data) {
        String follower;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();
            follower = object.get("follower").getAsString();
            String followee = object.get("followee").getAsString();

            try {
                String query = "DELETE FROM User_followers WHERE user_email=? AND followee_email=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, follower);
                    preparedStatement.setString(2, followee);
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(follower).getObject());
    }

    @Override
    public ReplyTuple listFollowers(String email, Integer limit, String order, Integer sinceId) {
        ArrayList<UserDataSet> followers = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select U.*, group_concat(distinct JUF.user_email) as followers, group_concat(distinct JUF1.followee_email) as following\n" );
            query.append("from User_followers as UF\n");
            query.append("join User as U on U.email=UF.user_email\n");
            query.append("left join User_followers as JUF on JUF.followee_email = U.email\n");
            query.append("left join User_followers as JUF1 on JUF1.user_email = U.email\n");
            query.append("where UF.followee_email = ?");


            if (sinceId != null) {
                query.append(" AND U.id > " + sinceId);
            }
            query.append(" group by email");
            if (order != null) {
                query.append(" ORDER BY U.name ");
                switch (order) {
                    case "asc":
                        query.append("ASC");
                        break;
                    case "desc":
                        query.append("DESC");
                        break;
                    default:
                        query.append("DESC");
                }
            } else {
                query.append(" ORDER BY U.name DESC");
            }
            if (limit != null) {
                query.append(" LIMIT " + limit);
            }
            try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setString(1, email);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        try {
                            followers.add(new UserDataSet(resultSet));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, followers);
    }

    @Override
    public ReplyTuple listFollowing(String email, Integer limit, String order, Integer sinceId) {
        ArrayList<UserDataSet> followers = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select U.*, group_concat(distinct JUF.user_email) as followers, group_concat(distinct JUF1.followee_email) as following\n" );
            query.append("from User_followers as UF\n");
            query.append("join User as U on U.email=UF.followee_email\n");
            query.append("left join User_followers as JUF on JUF.followee_email = U.email\n");
            query.append("left join User_followers as JUF1 on JUF1.user_email = U.email\n");
            query.append("where UF.user_email = ?");


            if (sinceId != null) {
                query.append(" AND U.id > " + sinceId);
            }
            query.append(" group by email");
            if (order != null) {
                query.append(" ORDER BY U.name ");
                switch (order) {
                    case "asc":
                        query.append("ASC");
                        break;
                    case "desc":
                        query.append("DESC");
                        break;
                    default:
                        query.append("DESC");
                }
            } else {
                query.append(" ORDER BY U.name DESC");
            }
            if (limit != null) {
                query.append(" LIMIT " + limit);
            }
            try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setString(1, email);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        try {
                            followers.add(new UserDataSet(resultSet));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, followers);
    }

    @Override
    public ArrayList<PostDataSet> listPosts(String email, Integer limit, String order, String since) {
        return null;
    }

    @Override
    public ReplyTuple updateProfile(String data) {
        String email;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            //Required fields
            email = object.get("user").getAsString();
            String name = object.get("name").getAsString();
            String about = object.get("about").getAsString();

            String query = "UPDATE User SET name=?, about=? WHERE email=?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, about);
                stmt.setString(3, email);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e ) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return details(email);
    }
}
