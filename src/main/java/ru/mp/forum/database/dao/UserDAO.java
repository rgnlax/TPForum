package ru.mp.forum.database.dao;

import ru.mp.forum.database.dao.impl.reply.Reply;

/**
 * Created by maksim on 08.01.16.
 */
public interface UserDAO extends BaseDAO {

    Reply create(String jsonString);

    Reply details(String email);

    Reply follow(String data);

    Reply unfollow(String data);

    Reply listFollowers(String email, Integer limit, String order, Integer sinceId);

    Reply listFollowing(String email, Integer limit, String order, Integer sinceId);

    Reply listPosts(String email, Integer limit, String order, String since);

    Reply updateProfile(String jsonString);
}
