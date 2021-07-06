package database.data_objects;

import java.sql.Timestamp;

public class MailComment {
    String name;
    String commentValue;
    Timestamp commentDate;

    public MailComment(String name, String commentValue, Timestamp commentDate) {
        this.name = name;
        this.commentValue = commentValue;
        this.commentDate = commentDate;
    }

    public String getName() {
        return name;
    }

    public String getCommentValue() {
        return commentValue;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }
}
