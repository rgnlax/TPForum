package ru.mp.forum.database.dao.impl;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.ThreadDAO;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maksim on 08.01.16.
 */
public class ThreadDAOImpl extends BaseDAOImpl implements ThreadDAO {

    public ThreadDAOImpl(Connection connection) {
        this.tableName = "Thread";
        this.connection = connection;
    }

    @Override
    public ReplyTuple create(String jsonString) {
        ThreadDataSet thread;
        try {
            thread = new ThreadDataSet(new JsonParser().parse(jsonString).getAsJsonObject());

            String query = "INSERT INTO  "+ tableName+"  (forum_short_name, title, isClosed, user_email, date, message, slug, isDeleted) VALUES (?,?,?,?,?,?,?,?);";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, thread.getForum().toString());
                stmt.setString(2, thread.getTitle());
                stmt.setBoolean(3, thread.getIsClosed());
                stmt.setString(4, thread.getUser().toString());
                stmt.setString(5, thread.getDate());
                stmt.setString(6, thread.getMessage());
                stmt.setString(7, thread.getSlug());
                stmt.setBoolean(8, thread.getIsDeleted());

                stmt.executeUpdate();

                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    resultSet.next();
                    thread.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, thread);
    }

    @Override
    public ReplyTuple details(int threadId, String[] related) {
        ThreadDataSet thread;
        try {
            String query = "SELECT  "+ tableName+".*, count(Post.id) as posts FROM  "+ tableName+"  \n" +
                    "LEFT JOIN Post on Post.thread_id =  "+ tableName+" .id\n" +
                    "WHERE  "+ tableName+" .id = ? AND Post.isDeleted = false";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, threadId);
                System.out.print(stmt.toString());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    resultSet.next();

                    thread = new ThreadDataSet(resultSet);
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }

        if (related != null) {
            if (Arrays.asList(related).contains("user")) {
                thread.setUser(new UserDAOImpl(connection).details(thread.getUser().toString()).getObject());
            }
            if (Arrays.asList(related).contains("forum")) {
                thread.setForum(new ForumDAOImpl(connection).details(thread.getForum().toString(), null).getObject());
            }
            if (Arrays.asList(related).contains("thread")) {
                return new ReplyTuple(Status.INCORRECT_REQUEST);
            }
        }
        return new ReplyTuple(Status.OK, thread);
    }

    @Override
    public ArrayList<PostDataSet> listPosts(int threadId, String since, Integer limit, String sort, String order) {
        return null;
    }

    @Override
    public ArrayList<ThreadDataSet> listUserThreads(String user, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ArrayList<ThreadDataSet> listForumThreads(String forum, String since, Integer limit, String order) {
        return null;
    }

    @Override
    public ReplyTuple remove(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer thread = object.get("thread").getAsInt();
            try {
                String query = "UPDATE  "+ tableName+"  SET isDeleted = 1 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, thread);
                    stmt.execute();
                }
                query = "UPDATE Post SET isDeleted = 1 WHERE thread_id = ?";
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

            Integer thread = object.get("thread").getAsInt();
            try {
                String query = "UPDATE  "+ tableName+"  SET isDeleted = 0 WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, thread);
                    stmt.execute();
                }
                query = "UPDATE Post SET isDeleted = 0 WHERE thread_id = ?";
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
        Integer thread;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String message = object.get("message").getAsString();
            String slug = object.get("slug").getAsString();
            thread = object.get("thread").getAsInt();

            String query = "UPDATE  "+ tableName+"  SET message = ?, slug = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, message);
                stmt.setString(2, slug);
                stmt.setInt(3, thread);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(thread, null).getObject());
    }

    @Override
    public ReplyTuple vote(String data) {
        Integer thread;
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer vote = object.get("vote").getAsInt();
            thread = object.get("thread").getAsInt();

            String column = vote == 1 ? "likes" : "dislikes";
            String query = "UPDATE  "+ tableName+"  SET " + column + " = " + column +  "+1 WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, thread);

                stmt.executeUpdate();
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new ReplyTuple(Status.INVALID_REQUEST);
        }
        return new ReplyTuple(Status.OK, details(thread, null).getObject());
    }

    @Override
    public ReplyTuple subscribe(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String user = object.get("user").getAsString();
            Integer thread = object.get("thread").getAsInt();

            try {
                String query = "INSERT INTO User_subscribes (user_email, thread_id) VALUES (?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, user);
                    stmt.setInt(2, thread);
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
    public ReplyTuple unsubscribe(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            String user = object.get("user").getAsString();
            Integer thread = object.get("thread").getAsInt();

            try {
                String query = "DELETE FROM User_subscribes WHERE user_email = ? AND thread_id = ?;";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, user);
                    stmt.setInt(2, thread);
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
    public ReplyTuple open(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer thread = object.get("thread").getAsInt();
            try {
                String query = "UPDATE  "+ tableName+"  SET isClosed = 0 WHERE id = ?";
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
    public ReplyTuple close(String data) {
        try {
            JsonObject object = new JsonParser().parse(data).getAsJsonObject();

            Integer thread = object.get("thread").getAsInt();
            try {
                String query = "UPDATE  "+ tableName+"  SET isClosed = 1 WHERE id = ?";
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
}
