package ru.mp.forum.database.dao.impl;

import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.ForumDAO;
import ru.mp.forum.database.dao.impl.reply.Reply;
import ru.mp.forum.database.data.ForumDataSet;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.data.UserDataSet;

import javax.sql.DataSource;
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

    public ForumDAOImpl(DataSource dataSource) {
        this.tableName = "Forum";
        this.dataSource = dataSource;
    }

    @Override
    public Reply create(String jsonString) {
        ForumDataSet forum;
        try (Connection connection = dataSource.getConnection()){
            forum = new ForumDataSet(new JsonParser().parse(jsonString).getAsJsonObject());

            String query = "INSERT INTO "+ tableName +" (name, short_name, user_email) VALUES (?, ?, ?)";
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
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, forum);
    }

    @Override
    public Reply details(String slug, String[] related) {
        ForumDataSet forum;
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM "+ tableName +" WHERE short_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, slug);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    resultSet.next();

                    forum = new ForumDataSet(resultSet);
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
            if (related != null) {
                if (Arrays.asList(related).contains("user")) {
                    forum.setUser(new UserDAOImpl(dataSource).details(forum.getUser().toString()).getObject());
                }
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }

        return new Reply(Status.OK, forum);
    }

    @Override
    public Reply listPosts(String forum, String since, Integer limit, String order, String[] related) {
        ArrayList<PostDataSet> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM Post");
            query.append(" WHERE forum_short_name = ?");
            if (since != null) {
                query.append(" AND date >= '" + since + "'");
            }
            if (order != null) {
                query.append(" ORDER BY date ");
                switch (order) {
                    case "asc": query.append(" ASC"); break;
                    case "desc": query.append(" DESC"); break;
                    default: query.append(" DESC");
                }
            } else {
                query.append(" ORDER BY date DESC");
            }
            if (limit != null) {
                query.append(" LIMIT " + limit);
            }
            try(PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setString(1, forum);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        PostDataSet post = new PostDataSet(resultSet);
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
                        posts.add(post);
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, posts);
    }

    @Override
    public Reply listThreads(String forum, String since, Integer limit, String order, String[] related) {
        ArrayList<ThreadDataSet> threads = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM Thread");
            query.append(" WHERE forum_short_name = ?");
            if (since != null) {
                query.append(" AND date >= '" + since + "'");
            }
            if (order != null) {
                query.append(" ORDER BY date ");
                switch (order) {
                    case "asc": query.append(" ASC"); break;
                    case "desc": query.append(" DESC"); break;
                    default: query.append(" DESC");
                }
            } else {
                query.append(" ORDER BY date DESC");
            }
            if (limit != null) {
                query.append(" LIMIT " + limit);
            }
            try(PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setString(1, forum);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        ThreadDataSet thread = new ThreadDataSet(resultSet);
                        if (related != null) {
                            if (Arrays.asList(related).contains("user")) {
                                thread.setUser(new UserDAOImpl(dataSource).details(thread.getUser().toString()).getObject());
                            }
                            if (Arrays.asList(related).contains("forum")) {
                                thread.setForum(new ForumDAOImpl(dataSource).details(thread.getForum().toString(), null).getObject());
                            }
                        }
                        threads.add(thread);
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, threads);
    }

    @Override
    public Reply listUsers(String forum, Integer sinceId, Integer limit, String order) {
        ArrayList<UserDataSet> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder query = new StringBuilder();
            query.append(" SELECT DISTINCT U.*,group_concat(distinct JUF.followee_email) as following, group_concat(distinct JUF1.user_email) as followers, group_concat(distinct JUS.thread_id) as subscribes\n FROM");
            query.append(" (SELECT DISTINCT user_email FROM Post WHERE forum_short_name = ?");
            if (sinceId != null) {
                query.append(" AND user_id >= " + sinceId);
            }
            if (order != null) {
                query.append(" ORDER BY user_name ");
                switch (order) {
                    case "asc": query.append("ASC"); break;
                    case "desc": query.append("DESC"); break;
                    default: query.append("DESC");
                }
            } else {
                query.append(" ORDER BY user_name DESC");
            }

            if (limit != null) {
                 query.append(" LIMIT "+ limit +") as T\n");
            } else {
                query.append(") as T\n");
            }

            query.append("  INNER JOIN User as U on U.email = T.user_email\n");
            query.append(" LEFT JOIN User_followers JUF ON email = JUF.user_email\n");
            query.append(" LEFT JOIN User_followers JUF1 ON email = JUF1.followee_email\n");
            query.append(" LEFT JOIN User_subscribes JUS ON email= JUS.user_email\n");

            query.append(" group by U.id");
            if (order != null) {
                query.append(" ORDER BY U.name ");
                switch (order) {
                    case "asc": query.append("ASC"); break;
                    case "desc": query.append("DESC"); break;
                    default: query.append("DESC");
                }
            } else {
                query.append(" ORDER BY U.name DESC");
            }

            try(PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                stmt.setString(1, forum);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        UserDataSet user = new UserDataSet(resultSet);

                        users.add(user);
                    }
                }
            } catch (SQLException e) {
                return handeSQLException(e);
            }
        } catch (Exception e) {
            return new Reply(Status.INVALID_REQUEST);
        }
        return new Reply(Status.OK, users);
    }
}
