package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.Reply;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.data.UserDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
*/
public interface ForumDAO extends  BaseDAO {
    Reply create(String jsonString);

    Reply details(String slug, String[] related);

    Reply listPosts(String forum, String since, Integer limit, String order, String[] related);

    Reply listThreads(String forum, String since, Integer limit, String order, String[] related);

    Reply listUsers(String forum, Integer sinceId, Integer limit, String order);
}
