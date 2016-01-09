package ru.mp.forum.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mp.forum.database.dao.ForumDAO;
import ru.mp.forum.database.dao.impl.ForumDAOImpl;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
public class ForumController extends BaseRestController {
    private ForumDAO forumDAO;

    @Override
    void init() {
        super.init();

        forumDAO = new ForumDAOImpl(con);
    }
}
