package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.ReplyTuple;

/**
 * Created by maksim on 08.01.16.
 */
public interface PostDAO extends  BaseDAO {
    ReplyTuple create(String jsonString);

    ReplyTuple details(int postId, String[] related);

    ReplyTuple listForumPosts(String forum, String since, Integer limit, String order);

    ReplyTuple listThreadPosts(int threadId, String since, Integer limit, String order);

    ReplyTuple remove(String jsonString);

    ReplyTuple restore(String jsonString);

    ReplyTuple update(String jsonString);

    ReplyTuple vote(String jsonString);
}

