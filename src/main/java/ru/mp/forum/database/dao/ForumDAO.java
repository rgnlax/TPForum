package ru.mp.forum.database.dao;

import ru.mp.forum.database.data.ForumDataSet;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.ThreadDataSet;
import ru.mp.forum.database.data.UserDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
*/
public interface ForumDAO {
    int getCount();

    void truncateTable();

    ForumDataSet create(String jsonString);

    ForumDataSet details(String forum, String[] related);

    ArrayList<PostDataSet> listPosts(String forum, String since, Integer limit, String order, String[] related);

    ArrayList<ThreadDataSet> listThreads(String forum, String since, Integer limit, String order, String[] related);

    ArrayList<UserDataSet> listUsers(String forum, Integer sinceId, Integer limit, String order);
}
