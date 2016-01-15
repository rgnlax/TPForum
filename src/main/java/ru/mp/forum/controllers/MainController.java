package ru.mp.forum.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mp.forum.controllers.response.RestResponse;
import ru.mp.forum.database.dao.MainDAO;
import ru.mp.forum.database.dao.impl.MainDAOImpl;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api")
public class MainController extends BaseRestController {
    private MainDAO mainDAO;

    @Override
    void init() {
        super.init();

        mainDAO = new MainDAOImpl(dataSource);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public RestResponse status() {
        return new RestResponse(mainDAO.getCount());
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public RestResponse clear() {
        mainDAO.truncateAll();
        return new RestResponse("OK");
    }

}
