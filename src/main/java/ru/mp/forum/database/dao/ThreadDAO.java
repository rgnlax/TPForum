package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.Reply;

/**
 * Created by maksim on 08.01.16.
 */
public interface ThreadDAO extends BaseDAO {

    Reply create(String jsonString);

    Reply details(int threadId, String[] related);

    Reply listPosts(int threadId, String since, Integer limit, String sort, String order);

    Reply listUserThreads(String user, String since, Integer limit, String order);

    Reply listForumThreads(String forum, String since, Integer limit, String order);

    Reply remove(String jsonString);

    Reply restore(String jsonString);

    Reply update(String jsonString);

    Reply vote(String jsonString);

    Reply subscribe(String data);

    Reply unsubscribe(String jsonString);

    Reply open(String jsonString);

    Reply close(String jsonString);
}
