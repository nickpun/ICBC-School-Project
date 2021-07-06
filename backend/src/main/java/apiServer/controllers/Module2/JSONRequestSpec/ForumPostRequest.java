package apiServer.controllers.Module2.JSONRequestSpec;

import java.sql.Timestamp;

public class ForumPostRequest {
    private String userId;
    private int categoryId;
    private String title;
    private Timestamp postDate;
    private String value;  // post content restricted to text

    public ForumPostRequest(String userId, Integer categoryId, String title, Timestamp postDate, String value) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.postDate = postDate;
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public String getValue() {
        return value;
    }
}
