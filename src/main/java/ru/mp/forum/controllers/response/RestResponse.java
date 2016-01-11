package ru.mp.forum.controllers.response;

import ru.mp.forum.database.dao.impl.reply.Reply;

import java.util.Map;

/**
 * Created by maksim on 09.01.16.
 */
public class RestResponse {
    private int code = Status.OK;
    private Object response;

    /**
     *  To make JSON response from Map
     */
    public RestResponse(Map response, int code) {
        this.response = response;
        this.code = code;
    }

    public RestResponse(Map response) {
        this.response = response;
    }

    /**
     * To make JSON response from String
     */
    public RestResponse(String response, int code) {
        this.response = response;
        this.code = code;
    }

    public RestResponse(String response) {
        this.response = response;
    }

    /**
     * To make error response
     */
    public RestResponse(int code) {
        this.code = code;
    }

    public RestResponse (Reply reply) {
        this.code = reply.getCode();
        this.response = reply.getObject();
    }

    public int getCode() {
        return code;
    }

    public Object getResponse() {
        return response;
    }
}
