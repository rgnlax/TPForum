package ru.mp.forum.database.dao;

import ru.mp.forum.database.data.PostDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface PostDAO extends  BaseDAO {

    int getCount(int threadId);

    PostDataSet create(String jsonString);

    PostDataSet details(int postId, String[] related);

    ArrayList<PostDataSet> listForumPosts(String forum, String since, Integer limit, String order);

    ArrayList<PostDataSet> listThreadPosts(int threadId, String since, Integer limit, String order);

    String remove(String jsonString);

    String restore(String jsonString);

    PostDataSet update(String jsonString);

    String vote(String jsonString);
}

