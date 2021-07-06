package database.data_objects;

import java.util.List;

public class Category {

    int categoryId;
    String categoryTitle;
    List<Post> posts;

    public Category(int categoryId, String categoryTitle, List<Post> posts) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.posts = posts;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
