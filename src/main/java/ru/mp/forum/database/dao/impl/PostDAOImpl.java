package ru.mp.forum.database.dao.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.PostDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDAOImpl extends BaseDAOImpl implements PostDAO {
    public PostDAOImpl(Connection connection) {
        this.tableName = "Post";
        this.connection = connection;
    }

    @Override
    public ReplyTuple create(String data) {
        PostDataSet post;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String forum = object.get("forum").getAsString();
            Integer thread = object.get("thread").getAsInt();
            String user = object.get("user").getAsString();
            String message = object.get("message").getAsString();
            String date = object.get("date").getAsString();
            Integer parent = object.has("parent") ? object.get("parent").getAsInt() : -1;
            boolean isApproved = object.has("isApproved") ? object.get("isApproved").getAsBoolean() : false;
            boolean isEdited = object.has("isEdited") ? object.get("isEdited").getAsBoolean() : false;
            boolean isHighlighted = object.has("isHighlighted") ? object.get("isHighlighted").getAsBoolean() : false;
            boolean isSpam = object.has("isSpam") ? object.get("isSpam").getAsBoolean() : false;
            boolean isDeleted = object.has("isDeleted") ? object.get("isDeleted").getAsBoolean() : false;

            post = new PostDataSet(0,date,thread,forum,user,message,parent,isEdited,isApproved,isHighlighted,isSpam,isDeleted,0,0,0);

            String query = "INSERT INTO Post (date, thread_id, forum_short_name, user_email, message, parent_id, isEdited, isApproved, isHighlighted, isDeleted, isSpam, likes, dislikes, points)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, post.getDate());
                stmt.setInt(2, thread);
                stmt.setString(3, post.getForum().toString());
                stmt.setString(4, post.getUser().toString());
                stmt.setString(5, post.getMessage());
                stmt.setInt(6, post.getParent());
                stmt.setBoolean(7, post.getIsEdited());
                stmt.setBoolean(8, post.getIsApproved());
                stmt.setBoolean(9, post.getIsHighlighted());
                stmt.setBoolean(10, post.getIsDeleted());
                stmt.setBoolean(11, post.getIsSpam());
                stmt.setInt(12, post.getLikes());
                stmt.setInt(13, post.getDislikes());
                stmt.setInt(14, post.getPoints());

                stmt.executeUpdate();
                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    resultSet.next();
                    post.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, post);
    }

    @Override
    public ReplyTuple details(int postId, String[] related) {
        PostDataSet post;
        try {
            String query = "SELECT * FROM Post WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, postId);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    resultSet.next();

                    post = new PostDataSet(resultSet);
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }

        if (related != null) {
            if (Arrays.asList(related).contains("user")) {
                post.setUser(new UserDAOImpl(connection).details(post.getUser().toString()).getObject());
            }
            if (Arrays.asList(related).contains("forum")) {
                post.setForum(new ForumDAOImpl(connection).details(post.getForum().toString(), null).getObject());
            }
            if (Arrays.asList(related).contains("thread")) {
                post.setThread(new ThreadDAOImpl(connection).details(Integer.parseInt(post.getThread().toString()), null).getObject());
            }
        }
        return new ReplyTuple(Status.OK, post);
    }

    @Override
    public ReplyTuple listForumPosts(String forum, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ReplyTuple listThreadPosts(int threadId, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ReplyTuple remove(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer thread = object.get("post").getAsInt();
            try {
                String query = "UPDATE Post SET isDeleted = 1 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, thread);
                    stmt.execute();
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, new Gson().fromJson(data, Object.class));
    }

    @Override
    public ReplyTuple restore(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer thread = object.get("post").getAsInt();
            try {
                String query = "UPDATE Post SET isDeleted = 0 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, thread);
                    stmt.execute();
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, new Gson().fromJson(data, Object.class));
    }

    @Override
    public ReplyTuple update(String data) {
        Integer post;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String message = object.get("message").getAsString();
            post = object.get("post").getAsInt();

            String query = "UPDATE Post SET message = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, message);
                stmt.setInt(2, post);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(post, null).getObject());
    }

    @Override
    public ReplyTuple vote(String data) {
        Integer post;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer vote = object.get("vote").getAsInt();
            post = object.get("post").getAsInt();

            String column = vote == 1 ? "likes" : "dislikes";
            String query = "UPDATE Post SET " + column + " = " + column +  "+1 WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, post);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(post, null).getObject());
    }
}
