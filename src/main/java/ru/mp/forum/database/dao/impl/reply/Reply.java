package ru.mp.forum.database.dao.impl.reply;


/**
 * Created by maksim on 09.01.16.
 */
public class Reply {
    private Integer code;
    private Object object;

    public Integer getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }

    public Reply(Integer code, Object object) {
        this.code = code;
        this.object = object;
    }

    public Reply(Integer code) {
        this.code = code;
        this.object = "";
    }
}
