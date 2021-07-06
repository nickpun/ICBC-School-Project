package database.data_objects;

import java.sql.Timestamp;
import java.util.List;

public class Post {

    int postId;
    String userId;
    String firstName;
    String lastName;
    int categoryId;
    String postTitle;
    Timestamp postDate;
    String postValue;
    int numOfComments;

    public Post(int postId, String userId, String firstName, String lastName, int categoryId, String postTitle,
                Timestamp postDate, String postValue, int numOfComments) {
        this.postId = postId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.categoryId = categoryId;
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.postValue = postValue;
        this.numOfComments = numOfComments;
    }

    public int getPostId() {
        return postId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public String getPostValue() {
        return postValue;
    }

    public int getNumOfComments() {
        return numOfComments;
    }
}
