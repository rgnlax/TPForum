package ru.mp.forum.database.data;

/**
 * Created by maksim on 08.01.16.
 */
public class ThreadDataSet {
    private final int id;
    private final String title;
    private final String date;
    private final String message;
    private final String slug;
    private final String forumShortName;
    private final String userEmail;
    private final int points;
    private final int likes;
    private final int dislikes;
    private final boolean isDeleted;
    private final boolean isClosed;

    public ThreadDataSet(int id, String title, String date, String message, String slug, String forumShortName, String userEmail, int points, int likes, int dislikes, boolean isDeleted, boolean isClosed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.message = message;
        this.slug = slug;
        this.forumShortName = forumShortName;
        this.userEmail = userEmail;
        this.points = points;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
    }

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSlug() {
        return slug;
    }

    public String getForumShortName() {
        return forumShortName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getPoints() {
        return points;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isClosed() {
        return isClosed;
    }
}
