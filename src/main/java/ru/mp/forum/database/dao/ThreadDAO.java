package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.Reply;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface ThreadDAO extends BaseDAO {

    Reply create(String jsonString);

    Reply details(int threadId, String[] related);

    ArrayList<PostDataSet> listPosts(int threadId, String since, Integer limit, String sort, String order);

    ArrayList<ThreadDataSet> listUserThreads(String user, String since, Integer limit, String order);

    ArrayList<ThreadDataSet> listForumThreads(String forum, String since, Integer limit, String order);

    Reply remove(String jsonString);

    Reply restore(String jsonString);

    Reply update(String jsonString);

    Reply vote(String jsonString);

    Reply subscribe(String data);

    Reply unsubscribe(String jsonString);

    Reply open(String jsonString);

    Reply close(String jsonString);
}
