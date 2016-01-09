package ru.mp.forum.database.dao;

import ru.mp.forum.database.data.PostDataSet;
import ru.mp.forum.database.data.UserDataSet;

import java.util.ArrayList;

/**
 * Created by maksim on 08.01.16.
 */
public interface UserDAO extends BaseDAO {

    UserDataSet create(String jsonString);

    UserDataSet details(String email);

    void follow(String follower, String followee);

    void unfollow(String follower, String followee);

    ArrayList<UserDataSet> listFollowers(String email, Integer limit, String order, Integer sinceId);

    ArrayList<UserDataSet> listFollowing(String email, Integer limit, String order, Integer sinceId);

    ArrayList<PostDataSet> listPosts(String email, Integer limit, String order, String since);

    void updateProfile(String jsonString);
}
