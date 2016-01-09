package ru.mp.forum.controllers;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.*;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.controllers.response.Status;
import ru.mp.forum.database.dao.UserDAO;
import ru.mp.forum.database.dao.impl.UserDAOImpl;
import ru.mp.forum.database.dao.impl.reply.ReplyTuple;
import ru.mp.forum.database.data.UserDataSet;

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

        userDAO = new UserDAOImpl(con);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RestResponse create(@RequestBody String data) {
        ReplyTuple reply = userDAO.create(data);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public RestResponse details(@RequestParam(value = "user") String email) {
        ReplyTuple reply = userDAO.details(email);
        return new RestResponse(reply);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public RestResponse follow(@RequestBody String data) {
        ReplyTuple reply = userDAO.follow(data);
        return new RestResponse(reply);
    }

}
