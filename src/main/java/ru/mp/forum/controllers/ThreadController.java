package ru.mp.forum.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.web.bind.annotation.*;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.database.dao.ThreadDAO;
import ru.mp.forum.database.dao.impl.ThreadDAOImpl;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api/thread")
public class ThreadController extends BaseRestController {
    private ThreadDAO threadDAO;

    @Override
    void init() {
        super.init();

        threadDAO = new ThreadDAOImpl(con);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RestResponse create(@RequestBody String body) {
        return new RestResponse(threadDAO.create(body));
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public RestResponse details(@RequestParam(value = "thread", required = true) int threadId,
                                @RequestParam(value = "related", required = false) String[] related) {
        return new RestResponse(threadDAO.details(threadId, related));
    }
    
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public RestResponse remove(@RequestBody String body) {
        return new RestResponse(threadDAO.remove(body));
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public RestResponse restore(@RequestBody String body) {
        return new RestResponse(threadDAO.restore(body));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResponse update(@RequestBody String body) {
        return new RestResponse(threadDAO.update(body));
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public RestResponse vote(@RequestBody String body) {
        return new RestResponse(threadDAO.vote(body));
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public RestResponse subscribe(@RequestBody String body) {
        return new RestResponse(threadDAO.subscribe(body));
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public RestResponse unsubscribe(@RequestBody String body) {
        return new RestResponse(threadDAO.unsubscribe(body));
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public RestResponse open(@RequestBody String body) {
        return new RestResponse(threadDAO.open(body));
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public RestResponse close(@RequestBody String body) {
        return new RestResponse(threadDAO.close(body));
    }
}

