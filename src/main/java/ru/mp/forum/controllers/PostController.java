package ru.mp.forum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.database.dao.PostDAO;
import ru.mp.forum.database.dao.impl.PostDAOImpl;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api/post")
public class PostController extends BaseRestController {
    private PostDAO postDAO;

    void init() {
        postDAO = new PostDAOImpl(con);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RestResponse create(@RequestBody String body){
        return new RestResponse(postDAO.create(body));
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public RestResponse details(@RequestParam(value = "post", required = true) int postId,
                                @RequestParam(value = "related", required = false) String[] related){

        return new RestResponse(postDAO.details(postId, related));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params={"forum"})
    public RestResponse listForumPosts(@RequestParam(value = "forum", required = true) String forum,
                                       @RequestParam(value = "since", required = false) String since,
                                       @RequestParam(value = "limit", required = false) Integer limit,
                                       @RequestParam(value = "order", required = false) String order){
        return new RestResponse(postDAO.listForumPosts(forum, since, limit, order));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params={"thread"})
    public RestResponse listThreadPosts(@RequestParam(value = "thread", required = true) int threadId,
                                        @RequestParam(value = "since", required = false) String since,
                                        @RequestParam(value = "limit", required = false) Integer limit,
                                        @RequestParam(value = "order", required = false) String order){
        return new RestResponse(postDAO.listThreadPosts(threadId, since, limit, order));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public RestResponse remove(@RequestBody String body){
        return new RestResponse(postDAO.remove(body));
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public RestResponse restore(@RequestBody String body){
        return new RestResponse(postDAO.restore(body));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResponse update(@RequestBody String body){
        return new RestResponse(postDAO.update(body));
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public RestResponse vote(@RequestBody String body){
        return new RestResponse(postDAO.vote(body));
    }
}
