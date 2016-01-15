package ru.mp.forum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by maksim on 09.01.16.
 */
@RestController
public abstract class BaseRestController {
    /**
     * MySQL database connection object
     */
    @Autowired
    protected DataSource dataSource;

    @PostConstruct
    void init() {
        /**
         * Initialization of dependencies
         */
    }
}
