package ru.mp.forum.database.dao.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.PostDAO;
import ru.mp.forum.database.dao.impl.reply.Reply;
import ru.mp.forum.database.data.PostDataSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDAOImpl extends BaseDAOImpl implements PostDAO {
    public PostDAOImpl(DataSource dataSource) {
        this.tableName = "Post";
        this.dataSource = dataSource;
    }

    @Override
    public Reply create(String data) {
        PostDataSet post;
        try (Connection connection = dataSource.getConnection()) {
            post = new PostDataSet(new JsonParser().parse(data).getAsJsonObject());

            String query = "INSERT INTO " + tableName + " (date, thread_id, forum_short_name, user_email, message, parent, isEdited, isApproved, isHighlighted, isDeleted, isSpam, likes, dislikes)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
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
        try (Connection connection = dataSource.getConnection()) {
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

            if (related != null) {
                if (Arrays.asList(related).contains("user")) {
                    post.setUser(new UserDAOImpl(dataSource).details(post.getUser().toString()).getObject());
                }
                if (Arrays.asList(related).contains("forum")) {
                    post.setForum(new ForumDAOImpl(dataSource).details(post.getForum().toString(), null).getObject());
                }
                if (Arrays.asList(related).contains("thread")) {
                    post.setThread(new ThreadDAOImpl(dataSource).details(Integer.parseInt(post.getThread().toString()), null).getObject());
                }
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }

        return new Reply(Status.OK, post);
    }

    @Override
    public Reply listForumPosts(String forum, String since, Integer limit, String order) {
        return new ForumDAOImpl(dataSource).listPosts(forum, since, limit, order, null);
    }

    @Override
    public Reply listThreadPosts(int threadId, String since, Integer limit, String order) {
        return new ThreadDAOImpl(dataSource).listPosts(threadId, since, limit, null, order);
    }

    @Override
    public Reply remove(String data) {
        try (Connection connection = dataSource.getConnection()) {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer post = object.get("post").getAsInt();
            try {
                String query = "UPDATE " + tableName + " SET isDeleted = 1 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, post);
                    stmt.executeUpdate();
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
        try (Connection connection = dataSource.getConnection()) {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer post = object.get("post").getAsInt();
            try {
                String query = "UPDATE " + tableName + " SET isDeleted = 0 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, post);
                    stmt.executeUpdate();
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
        try (Connection connection = dataSource.getConnection()) {
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
        try (Connection connection = dataSource.getConnection()) {
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

    @Deprecated
    private void updateThread(boolean inc, int id) {
        try (Connection connection = dataSource.getConnection()) {
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

    @Deprecated
    private void updatePath(PostDataSet post) {
        String materializedPath = "";

        String query = "SELECT m_path FROM " + tableName + " WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, (post.getParent() == null ? 0 : post.getParent()));

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        materializedPath = resultSet.getString("m_path");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            materializedPath += "/";
            materializedPath += Integer.toString(post.getId(), 36);

            query = "UPDATE "+tableName+" SET m_path = ? WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, materializedPath);
                stmt.setInt(2, post.getId());

                stmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
