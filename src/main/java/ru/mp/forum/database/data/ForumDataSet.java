package ru.mp.forum.database.data;

/**
 * Created by maksim on 08.01.16.
 */
public class ForumDataSet {
    private final String shortName;
    private final int id;
    private final String userEmail;
    private final String name;

    public ForumDataSet(String shortName, int id, String userEmail, String name) {
        this.shortName = shortName;
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }
}
