package ru.mp.forum.database.data;

/**
 * Created by maksim on 08.01.16.
 */
public class PostDataSet {
    private final int id;
    private final String date;
    private final int threadID;
    private final String forumShortName;
    private final String userEmail;
    private final String message;
    private final int parentID;
    private final boolean isEdited;
    private final boolean isApproved;
    private final boolean isHighlighted;
    private final boolean isSpam;
    private final int likes;
    private final int dislikes;
    private final int points;

    public PostDataSet(int id, String date, int threadID, String forumShortName, String userEmail, String message, int parentID, boolean isEdited, boolean isApproved, boolean isHighlighted, boolean isSpam, int likes, int dislikes, int points) {
        this.id = id;
        this.date = date;
        this.threadID = threadID;
        this.forumShortName = forumShortName;
        this.userEmail = userEmail;
        this.message = message;
        this.parentID = parentID;
        this.isEdited = isEdited;
        this.isApproved = isApproved;
        this.isHighlighted = isHighlighted;
        this.isSpam = isSpam;
        this.likes = likes;
        this.dislikes = dislikes;
        this.points = points;
    }


    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getThreadID() {
        return threadID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getMessage() {
        return message;
    }

    public int getParentID() {
        return parentID;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getPoints() { return points; }

    public String getForumShortName() { return forumShortName; }
}
