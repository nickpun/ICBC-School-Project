package database.data_objects;

import java.sql.Timestamp;

public class Comment {

    String userId;
    String firstName;
    String lastName;
    int postId;
    Timestamp commentDate;
    String commentValue;
    int commentId;

    public Comment(String userId, String firstName, String lastName, int postId, Timestamp commentDate, String commentValue, int commentId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postId = postId;
        this.commentDate = commentDate;
        this.commentValue = commentValue;
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPostId() {
        return postId;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public String getCommentValue() {
        return commentValue;
    }

    public int getCommentId() { return commentId; }
}
