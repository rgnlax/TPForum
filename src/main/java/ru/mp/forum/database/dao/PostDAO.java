package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.Reply;

/**
 * Created by maksim on 08.01.16.
 */
public interface PostDAO extends  BaseDAO {
    Reply create(String jsonString);

    Reply details(int postId, String[] related);

    Reply listForumPosts(String forum, String since, Integer limit, String order);

    Reply listThreadPosts(int threadId, String since, Integer limit, String order);

    Reply remove(String jsonString);

    Reply restore(String jsonString);

    Reply update(String jsonString);

    Reply vote(String jsonString);
}

