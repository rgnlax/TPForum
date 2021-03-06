package ru.mp.forum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.dao.impl.UserDAOImpl;
import ru.mp.forum.database.dao.impl.reply.Reply;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api/user")
public class UserController extends BaseRestController {
    private UserDAO userDAO;

    @Override
    void init() {
        super.init();

        userDAO = new UserDAOImpl(dataSource);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RestResponse create(@RequestBody String data) {
        Reply reply = userDAO.create(data);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public RestResponse details(@RequestParam(value = "user") String email) {
        Reply reply = userDAO.details(email);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public RestResponse follow(@RequestBody String data) {
        Reply reply = userDAO.follow(data);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    public RestResponse unfollow(@RequestBody String data) {
        Reply reply = userDAO.unfollow(data);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public RestResponse updateProfile(@RequestBody String data) {
        Reply reply = userDAO.updateProfile(data);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/listFollowers", method = RequestMethod.GET)
    public RestResponse listFollowers(@RequestParam(value = "user", required = true) String email,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "since_id", required = false) Integer sinceId) {
        Reply reply = userDAO.listFollowers(email, limit, order, sinceId);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/listFollowing", method = RequestMethod.GET)
    public RestResponse listFollowing(@RequestParam(value = "user", required = true) String email,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "since_id", required = false) Integer sinceId) {
        Reply reply = userDAO.listFollowing(email, limit, order, sinceId);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/listPosts", method = RequestMethod.GET)
    public RestResponse listPosts(@RequestParam(value = "user", required = true) String email,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "since", required = false) String since){
        return new RestResponse(userDAO.listPosts(email, limit, order, since));
    }


}
