package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.data_objects.*;
import org.junit.*;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

public class ModuleTwoQueriesSpec {

    private static String userId = "100000";
    private static String firstName = "Bob";
    private static String lastName = "The Builder";
    private static String dept = "Software";

    private static int categoryId;
    private static String categoryTitle = "Covid-19";

    private static int postId;
    private static Timestamp postDate = new Timestamp(1613779200000L + 28800000L);
    private static String postTitle = "Wash your hands!";
    private static String postValue = "Wash your hands for 20 seconds";

    private static Integer commentId;
    private static Timestamp commentDate = new Timestamp(1613865600000L + 28800000L);
    private static String commentValue = "Or sanitize!";

    @BeforeClass
    public static void beforeAll() {
        try {
            int rowsAffected = AdministrativeQueries.addEmployee(userId, firstName, lastName, dept);
            Assert.assertEquals(1, rowsAffected);
            categoryId = ModuleTwoQueries.addCategory(categoryTitle);
            postId = ModuleTwoQueries.addPost(userId, categoryId, postTitle,
                    postDate, postValue);
            commentId = ModuleTwoQueries.addComment(userId, postId, commentDate, commentValue);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetCategoryAndPosts() {
        Category resCategory = null;

        try {
            resCategory = ModuleTwoQueries.getCategory(categoryId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        List<Post> posts = resCategory.getPosts();
        Assert.assertEquals(resCategory.getCategoryId(), categoryId);
        Assert.assertEquals(resCategory.getCategoryTitle(), categoryTitle);
        Assert.assertEquals(1, posts.size());

        Post post = posts.get(0);
        Assert.assertEquals(postId, post.getPostId());
        Assert.assertEquals(userId, post.getUserId());
        Assert.assertEquals(categoryId, post.getCategoryId());
        Assert.assertEquals(postDate.getTime(),
                post.getPostDate().getTime());
        Assert.assertEquals(postTitle, post.getPostTitle());
        Assert.assertEquals(postValue, post.getPostValue());
        Assert.assertEquals(1, post.getNumOfComments());
        Assert.assertEquals(firstName, post.getFirstName());
        Assert.assertEquals(lastName, post.getLastName());
    }

    @Test
    public void testGetPostAndComments() {
        Post post = null;

        try {
            post = ModuleTwoQueries.getPost(postId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Assert.assertEquals(postId, post.getPostId());
        Assert.assertEquals(userId, post.getUserId());
        Assert.assertEquals(categoryId, post.getCategoryId());
        Assert.assertEquals(postDate.getTime(), post.getPostDate().getTime());
        Assert.assertEquals(postTitle, post.getPostTitle());
        Assert.assertEquals(postValue, post.getPostValue());
        Assert.assertEquals(1, post.getNumOfComments());
        Assert.assertEquals(firstName, post.getFirstName());
        Assert.assertEquals(lastName, post.getLastName());

        List<Comment> comments = null;
        try {
            comments = ModuleTwoQueries.getComments(postId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Comment comment = comments.get(0);
        Assert.assertEquals(commentValue, comment.getCommentValue());
        Assert.assertEquals(commentDate.getTime(),
                comment.getCommentDate().getTime());

    }

    @Test
    public void testGetPreviewCategories() {
        try {
            String categoryTitle2 = "testCategory2";
            String categoryTitle3 = "testCategory3";
            int categoryId2 = ModuleTwoQueries.addCategory(categoryTitle2);
            int categoryId3 = ModuleTwoQueries.addCategory(categoryTitle3);

            String testPost2 = "testPost2";
            String testPost3 = "testPost3";
            String testPost4 = "testPost4";
            String testPost5 = "testPost5";
            Timestamp postDate2 = new Timestamp(1613865602000L);
            Timestamp postDate3 = new Timestamp(1613865603000L);
            Timestamp postDate4 = new Timestamp(1613865604000L);
            Timestamp postDate5 = new Timestamp(1613865605000L);
            ModuleTwoQueries.addPost(userId, categoryId2, testPost2, postDate2, postValue);
            int postId3 = ModuleTwoQueries.addPost(userId, categoryId2, testPost3, postDate3, postValue);
            int postId4 = ModuleTwoQueries.addPost(userId, categoryId2, testPost4, postDate4, postValue);
            int postId5 = ModuleTwoQueries.addPost(userId, categoryId2, testPost5, postDate5, postValue);

            ModuleTwoQueries.addComment(userId, postId5, postDate5, "hello");

            List<Category> categoryList = ModuleTwoQueries.getPreviewCategories();

            for (Category category : categoryList) {
                if (category.getCategoryId() == categoryId2) {
                    Assert.assertEquals(categoryTitle2, category.getCategoryTitle());
                    Assert.assertEquals(3, category.getPosts().size());
                    Assert.assertEquals(postDate5.getTime(),
                            category.getPosts().get(0).getPostDate().getTime());
                    Assert.assertEquals(postDate4.getTime(),
                            category.getPosts().get(1).getPostDate().getTime());
                    Assert.assertEquals(postDate3.getTime(),
                            category.getPosts().get(2).getPostDate().getTime());

                    Assert.assertEquals(postId5, category.getPosts().get(0).getPostId());
                    Assert.assertEquals(postId4, category.getPosts().get(1).getPostId());
                    Assert.assertEquals(postId3, category.getPosts().get(2).getPostId());

                    Assert.assertEquals(testPost5, category.getPosts().get(0).getPostTitle());
                    Assert.assertEquals(testPost4, category.getPosts().get(1).getPostTitle());
                    Assert.assertEquals(testPost3, category.getPosts().get(2).getPostTitle());

                    Assert.assertEquals(1, category.getPosts().get(0).getNumOfComments());
                } else if (category.getCategoryId() == categoryId3) {
                    Assert.assertEquals(categoryTitle3, category.getCategoryTitle());
                    Assert.assertEquals(0, category.getPosts().size());
                }
            }

            int rowsAffected1 = ModuleTwoQueries.deleteCategory(categoryId2);
            Assert.assertEquals(1, rowsAffected1);
            int rowsAffected2 = ModuleTwoQueries.deleteCategory(categoryId3);
            Assert.assertEquals(1, rowsAffected2);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

    }

    @AfterClass
    public static void afterAll() {
        try {
            int commentRowsAffected = ModuleTwoQueries.deleteComment(commentId);
            Assert.assertEquals(1, commentRowsAffected);
            int postRowsAffected = ModuleTwoQueries.deletePost(postId);
            Assert.assertEquals(1, postRowsAffected);
            int categoryRowsAffected = ModuleTwoQueries.deleteCategory(categoryId);
            Assert.assertEquals(1, categoryRowsAffected);
            int employeeRowsAffected = AdministrativeQueries.deleteEmployee(userId);
            Assert.assertEquals(1, employeeRowsAffected);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
