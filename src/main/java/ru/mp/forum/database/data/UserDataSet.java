package ru.mp.forum.database.data;

/**
 * Created by maksim on 08.01.16.
 */
public class UserDataSet {
    private final String email;
    private final int id;
    private final String username;
    private final String name;
    private final String about;
    private final boolean isAnonymous;

    public UserDataSet(String email, int id, String username, String name, String about, boolean isAnonymous) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.name = name;
        this.about = about;
        this.isAnonymous = isAnonymous;
    }

    public String getEmail() { return email; }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
}
