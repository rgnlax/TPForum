package ru.mp.forum.database.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.dao.impl.reply.Reply;
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
    public Reply create(String data) {
        UserDataSet user;
        try {
            user = new UserDataSet(new JsonParser().parse(data).getAsJsonObject());

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
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, user);
    }

    @Override
    public Reply details(String email) {
        UserDataSet user;
        String query = "SELECT U.*, group_concat(distinct JUF.followee_email) as following, group_concat(distinct JUF1.user_email) as followers, group_concat(distinct JUS.thread_id) as subscribes\n" +
                "FROM User U \n" +
                "LEFT JOIN User_followers JUF ON U.email = JUF.user_email\n" +
                "LEFT JOIN User_followers JUF1 ON U.email = JUF1.followee_email\n" +
                "LEFT JOIN User_subscribes JUS ON U.email= JUS.user_email\n" +
                "WHERE U.email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();

                user = new UserDataSet(resultSet);

            } catch (Exception e) {
                return new Reply(Status.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new Reply(Status.INCORRECT_REQUEST);
        }
        return new Reply(Status.OK, user);
    }

    @Override
    public Reply follow(String data) {
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
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, details(follower).getObject());
    }

    @Override
    public Reply unfollow(String data) {
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
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, details(follower).getObject());
    }

    @Override
    public Reply listFollowers(String email, Integer limit, String order, Integer sinceId) {
        ArrayList<UserDataSet> followers = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select U.*, group_concat(distinct JUF.user_email) as followers, group_concat(distinct JUF1.followee_email) as following, group_concat(distinct JUS.thread_id) as subscribes\n" );
            query.append("from User_followers as UF\n");
            query.append("join User as U on U.email=UF.user_email\n");
            query.append("left join User_followers as JUF on JUF.followee_email = U.email\n");
            query.append("left join User_followers as JUF1 on JUF1.user_email = U.email\n");
            query.append("left join User_subscribes as JUS on JUS.user_email = U.email\n");
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
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, followers);
    }

    @Override
    public Reply listFollowing(String email, Integer limit, String order, Integer sinceId) {
        ArrayList<UserDataSet> followers = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select U.*, group_concat(distinct JUF.user_email) as followers, group_concat(distinct JUF1.followee_email) as following, group_concat(distinct JUS.thread_id) as subscribes\n" );
            query.append("from User_followers as UF\n");
            query.append("join User as U on U.email=UF.followee_email\n");
            query.append("left join User_followers as JUF on JUF.followee_email = U.email\n");
            query.append("left join User_followers as JUF1 on JUF1.user_email = U.email\n");
            query.append("left join User_subscribes as JUS on JUS.user_email = U.email\n");
            query.append("where UF.user_email = ?");


            if (sinceId != null) {
                query.append(" AND U.id >= " + sinceId);
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
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, followers);
    }

    @Override
    public ArrayList<PostDataSet> listPosts(String email, Integer limit, String order, String since) {
        return null;
    }

    @Override
    public Reply updateProfile(String data) {
        String email;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            //Required fields
            email = object.get("user").getAsString();
            String name = object.get("name").getAsString();
            String about = object.get("about").getAsString();

            String query = "UPDATE " + tableName + " SET name=?, about=? WHERE email=?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, about);
                stmt.setString(3, email);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e ) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return details(email);
    }
}
