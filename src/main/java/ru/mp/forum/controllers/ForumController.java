package ru.mp.forum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.database.dao.ForumDAO;
import ru.mp.forum.database.dao.impl.ForumDAOImpl;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api/forum")
public class ForumController extends BaseRestController {
    private ForumDAO forumDAO;

    @Override
    void init() {
        super.init();

        forumDAO = new ForumDAOImpl(dataSource);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RestResponse create(@RequestBody String body){
        return new RestResponse(forumDAO.create(body));
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public RestResponse details(@RequestParam(value = "forum", required = true) String slug,
                                @RequestParam(value = "related", required = false) String[] related ) {
        return new RestResponse(forumDAO.details(slug, related));
    }

    @RequestMapping(value = "/listPosts", method = RequestMethod.GET)
    public RestResponse listPosts(@RequestParam(value = "forum", required = true) String forum,
                                          @RequestParam(value = "since", required = false) String since,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "related", required = false) String[] related){
        return new RestResponse(forumDAO.listPosts(forum, since, limit, order, related));
    }

    @RequestMapping(value = "/listThreads", method = RequestMethod.GET)
    public RestResponse listThreads(@RequestParam(value = "forum", required = true) String forum,
                                            @RequestParam(value = "since", required = false) String since,
                                            @RequestParam(value = "limit", required = false) Integer limit,
                                            @RequestParam(value = "order", required = false) String order,
                                            @RequestParam(value = "related", required = false) String[] related){
        return new RestResponse(forumDAO.listThreads(forum, since, limit, order, related));
    }

    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    public RestResponse listUsers(@RequestParam(value = "forum", required = true) String forum,
                                          @RequestParam(value = "since_id", required = false) Integer sinceId,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "order", required = false) String order){
        return new RestResponse(forumDAO.listUsers(forum, sinceId, limit, order));
    }

}
