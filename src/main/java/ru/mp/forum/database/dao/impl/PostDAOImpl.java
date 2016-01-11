package ru.mp.forum.database.dao.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.PostDAO;
import ru.mp.forum.database.dao.impl.reply.Reply;
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
    public Reply create(String data) {
        PostDataSet post;
        try {
            post = new PostDataSet(new JsonParser().parse(data).getAsJsonObject());

            String query = "INSERT INTO " + tableName + " (date, thread_id, forum_short_name, user_email, message, m_path, isEdited, isApproved, isHighlighted, isDeleted, isSpam, likes, dislikes)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, post.getDate());
                stmt.setInt(2, (Integer)post.getThread());
                stmt.setString(3, post.getForum().toString());
                stmt.setString(4, post.getUser().toString());
                stmt.setString(5, post.getMessage());
                stmt.setObject(6, post.getParent());
                stmt.setBoolean(7, post.getIsEdited());
                stmt.setBoolean(8, post.getIsApproved());
                stmt.setBoolean(9, post.getIsHighlighted());
                stmt.setBoolean(10, post.getIsDeleted());
                stmt.setBoolean(11, post.getIsSpam());
                stmt.setInt(12, post.getLikes());
                stmt.setInt(13, post.getDislikes());

                stmt.executeUpdate();
                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    resultSet.next();
                    post.setId(resultSet.getInt(1));

                    if (stmt.getUpdateCount() > 0 && !post.getIsDeleted()) {
                        updateThread(true, (Integer)post.getId());
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, post);
    }

    @Override
    public Reply details(int postId, String[] related) {
        PostDataSet post;
        try {
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
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
            return new Reply(Status.INVALID_REQUEST);
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
        return new Reply(Status.OK, post);
    }

    @Override
    public Reply listForumPosts(String forum, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public Reply listThreadPosts(int threadId, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public Reply remove(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer post = object.get("post").getAsInt();
            try {
                String query = "UPDATE " + tableName + " SET isDeleted = 1 WHERE id = ? AND isDeleted = 0";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, post);
                    stmt.executeUpdate();

                    if (stmt.getUpdateCount() > 0) {
                        updateThread(false, post);
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, new Gson().fromJson(data, Object.class));
    }

    @Override
    public Reply restore(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer post = object.get("post").getAsInt();
            try {
                String query = "UPDATE " + tableName + " SET isDeleted = 0 WHERE id = ? AND isDeleted = 1";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, post);
                    stmt.executeUpdate();

                    if (stmt.getUpdateCount() > 0) {
                        updateThread(true, post);
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, new Gson().fromJson(data, Object.class));
    }

    @Override
    public Reply update(String data) {
        Integer post;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String message = object.get("message").getAsString();
            post = object.get("post").getAsInt();

            String query = "UPDATE " + tableName + " SET message = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, message);
                stmt.setInt(2, post);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, details(post, null).getObject());
    }

    @Override
    public Reply vote(String data) {
        Integer post;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer vote = object.get("vote").getAsInt();
            post = object.get("post").getAsInt();

            String column = vote == 1 ? "likes" : "dislikes";
            String query = "UPDATE " + tableName + " SET " + column + " = " + column +  "+1 WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, post);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, details(post, null).getObject());
    }

    private void updateThread(boolean inc, int id) {
        try {
            String operation = inc ? "+" : "-";
            String query = "UPDATE Thread SET posts=posts "+ operation +" 1 WHERE id = (SELECT thread_id FROM Post WHERE Post.id = ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);

                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
