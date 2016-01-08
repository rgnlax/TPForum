package ru.mp.forum.database.dao;

import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface ThreadDAO {
    int getCount();

    void truncateTable();

    ThreadDataSet create(String jsonString);

    ThreadDataSet details(int threadId, String[] related);

    ArrayList<PostDataSet> listPosts(int threadId, String since, Integer limit, String sort, String order);

    ArrayList<ThreadDataSet> listUserThreads(String user, String since, Integer limit, String order);

    ArrayList<ThreadDataSet> listForumThreads(String forum, String since, Integer limit, String order);

    String remove(String jsonString);

    String restore(String jsonString);

    ThreadDataSet update(String jsonString);

    ThreadDataSet vote(String jsonString);

    String subscribe(String jsonString);

    String unsubscribe(String jsonString);

    String open(String jsonString);

    String close(String jsonString);
}
