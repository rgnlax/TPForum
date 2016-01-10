package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.UserDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface UserDAO extends BaseDAO {

    ReplyTuple create(String jsonString);

    ReplyTuple details(String email);

    ReplyTuple follow(String data);

    ReplyTuple unfollow(String data);

    ReplyTuple listFollowers(String email, Integer limit, String order, Integer sinceId);

    ReplyTuple listFollowing(String email, Integer limit, String order, Integer sinceId);

    ArrayList<PostDataSet> listPosts(String email, Integer limit, String order, String since);

    ReplyTuple updateProfile(String jsonString);
}
