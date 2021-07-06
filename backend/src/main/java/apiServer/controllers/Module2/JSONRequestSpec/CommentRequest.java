package apiServer.controllers.Module2.JSONRequestSpec;

import java.sql.Timestamp;

public class CommentRequest {
    private String userId;
    private int postId;
    private Timestamp commentDate;
    private String value;  // comment content confined to text

    public CommentRequest(String userId, Integer postId, Timestamp commentDate, String value) {
        this.userId = userId;
        this.postId = postId;
        this.commentDate = commentDate;
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public String getValue() {
        return value;
    }
}
