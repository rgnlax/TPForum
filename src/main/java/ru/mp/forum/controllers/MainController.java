package ru.mp.forum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.Connection;

/**
 * Created by maksim on 08.01.16.
 */
@RestController
@RequestMapping(value = "/db/api")
public class MainController {
    @Autowired
    private Connection connection;

    @PostConstruct
    void init() {
        //TODO
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status(){
        return null;
    }

}
