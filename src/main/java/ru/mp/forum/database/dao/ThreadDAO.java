package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface ThreadDAO extends BaseDAO {

    ReplyTuple create(String jsonString);

    ReplyTuple details(int threadId, String[] related);

    ArrayList<PostDataSet> listPosts(int threadId, String since, Integer limit, String sort, String order);

    ArrayList<ThreadDataSet> listUserThreads(String user, String since, Integer limit, String order);

    ArrayList<ThreadDataSet> listForumThreads(String forum, String since, Integer limit, String order);

    ReplyTuple remove(String jsonString);

    ReplyTuple restore(String jsonString);

    ReplyTuple update(String jsonString);

    ReplyTuple vote(String jsonString);

    ReplyTuple subscribe(String data);

    ReplyTuple unsubscribe(String jsonString);

    ReplyTuple open(String jsonString);

    ReplyTuple close(String jsonString);
}
