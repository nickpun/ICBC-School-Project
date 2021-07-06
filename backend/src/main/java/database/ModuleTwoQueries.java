package database;

import com.mysql.cj.protocol.Resultset;
import database.data_objects.Category;
import database.data_objects.Comment;
import database.data_objects.Post;

import java.sql.*;
import java.util.*;

public class ModuleTwoQueries {

    public static Category getCategory(Integer categoryId) throws SQLException {
        String query = "SELECT category_title FROM Categories WHERE category_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            rs = statement.executeQuery();

            rs.next();
            String title = rs.getString(1);
            List<Post> posts = getPosts(categoryId);
            return new Category(categoryId, title, posts);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    private static List<Post> getPosts(Integer categoryId) throws SQLException {
        String query = "SELECT P.post_id, P.user_id, E.first_name, E.last_name, " +
                       "P.post_title, P.post_date, P.post_value\n" +
                       "FROM Posts P, Employees E\n" +
                       "WHERE P.category_id = ? AND P.user_id = E.user_id";
        PreparedStatement statement = null;
        ResultSet rs = null;

        PreparedStatement statement2 = null;
        ResultSet rs2 = null;
        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            rs = statement.executeQuery();

            List<Post> postList = new ArrayList<>();
            while (rs.next()) {
                int postId = rs.getInt(1);
                String userId = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String postTitle = rs.getString(5);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp postDate = rs.getTimestamp(6, calendar);
                String postVal = rs.getString(7);

                String query2 = "SELECT COUNT(post_id) FROM Comments WHERE post_id = ?";
                statement2 = connection.prepareStatement(query2);
                statement2.setInt(1, postId);
                rs2 = statement2.executeQuery();

                rs2.next();
                int numComments = rs2.getInt(1);
                Post post = new Post(postId, userId, firstName, lastName,
                        categoryId, postTitle, postDate, postVal, numComments);
                postList.add(post);
            }
            return postList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (rs2 != null) rs2.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement2 != null) statement2.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    public static Post getPost(Integer postId) throws SQLException {
        String query = "SELECT P.user_id, E.first_name, E.last_name, " +
                       "P.post_title, P.post_date, P.post_value, P.category_id\n" +
                       "FROM Posts P, Employees E\n" +
                       "WHERE P.post_id = ? AND P.user_id = E.user_id\n" +
                       "ORDER BY P.post_date DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, postId);
            rs = statement.executeQuery();

            rs.next();
            String userId = rs.getString(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String postTitle = rs.getString(4);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            Timestamp postDate = rs.getTimestamp(5, calendar);
            String postVal = rs.getString(6);
            int categoryId = rs.getInt(7);
            int numComments = getComments(postId).size();

            return new Post(postId, userId, firstName, lastName, categoryId,
                    postTitle, postDate, postVal, numComments);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }


    public static List<Comment> getComments(Integer postId) throws SQLException {
        String query = "SELECT C.user_id, E.first_name, E.last_name, C.comment_date, C.comment_value, C.comment_id \n" +
                "FROM Comments C, Employees E \n" +
                "WHERE C.post_id = ? AND C.user_id = E.user_id\n" +
                "ORDER BY C.comment_date DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, postId);
            rs = statement.executeQuery();

            List<Comment> comments = new ArrayList<>();
            while(rs.next()) {
                String userId = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp commentDate = rs.getTimestamp(4, calendar);
                String commentVal = rs.getString(5);
                int commentId = rs.getInt(6);

                Comment comment = new Comment(userId, firstName, lastName, postId, commentDate, commentVal, commentId);
                comments.add(comment);
            }
            return comments;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    public static List<Category> getPreviewCategories() throws SQLException {
        String query1 = "SELECT category_id, category_title\n" +
                        "FROM Categories";
        String query2 = "SELECT P.post_id, P.user_id, P.post_title, P.post_date, P.post_value, " +
                        "E.first_name, E.last_name\n" +
                        "FROM Posts P, Employees E\n" +
                        "WHERE P.category_id = ?\n" +
                        "AND P.user_id = E.user_id\n" +
                        "ORDER BY P.post_date DESC LIMIT 3";
        String query3 = "SELECT COUNT(*)\n" +
                        "FROM Comments\n" +
                        "WHERE post_id = ?";

        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement1 = connection.prepareStatement(query1);
            rs1 = statement1.executeQuery();

            Map<Integer, String> categoryMap = new HashMap<>();
            while(rs1.next()) {
                categoryMap.put(rs1.getInt(1), rs1.getString(2));
            }

            List<Category> categoryList = new ArrayList<>();
            for (Map.Entry<Integer, String> pair : categoryMap.entrySet()) {
                statement2 = connection.prepareStatement(query2);
                statement2.setInt(1, pair.getKey());
                rs2 = statement2.executeQuery();

                List<Post> tempPostList = new ArrayList<>();
                while(rs2.next()) {
                    int postId = rs2.getInt(1);
                    String userId = rs2.getString(2);
                    String postTitle = rs2.getString(3);
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                    Timestamp postDate = rs2.getTimestamp(4, calendar);
                    String postValue = rs2.getString(5);
                    String firstName = rs2.getString(6);
                    String lastName = rs2.getString(7);

                    statement3 = connection.prepareStatement(query3);
                    statement3.setInt(1, postId);
                    rs3 = statement3.executeQuery();

                    rs3.next();
                    int numOfComments = rs3.getInt(1);

                    Post tempPost = new Post(postId, userId, firstName, lastName, pair.getKey(),
                            postTitle, postDate, postValue, numOfComments);
                    tempPostList.add(tempPost);
                }

                Category tempCategory = new Category(pair.getKey(), pair.getValue(), tempPostList);
                categoryList.add(tempCategory);
            }
            return categoryList;

        } finally {
            try { if (rs1 != null) rs1.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (rs2 != null) rs2.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (rs3 != null) rs3.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement1 != null) statement1.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement2 != null) statement2.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement3 != null) statement2.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that add categories
    * @params title: This is the name of the Category
    * @return Integer: The category_id of the category that is being created
    * @exception SQLException: Failure to connect to database or query fails)
    */
    public static Integer addCategory(String title) throws SQLException {
        String query = "INSERT INTO Categories (category_title)\n" +
                       "VALUES(?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that adds comments on posts
    * @param userId: the ID of the user that is commenting on a particular post
    * @param postId: the ID of the post that the user is commenting on
    * @param comment_date: the date on which the user is commenting on
    * @param value: the comment itself
    * @return Integer: the comment_id of the comment that is being created
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static Integer addComment(String userId, int postId,
                                        Timestamp commentDate, String value) throws SQLException {
        String query = "INSERT INTO Comments (user_id, post_id, comment_date, comment_value)\n" +
                       "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setInt(2, postId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement.setTimestamp(3, commentDate, calendar);
            statement.setString(4, value);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that adds posts
    * @param userId: the ID of the user that is creating the post
    * @param categoryId: the ID of the category under which the post comes
    * @param title: the title of the post
    * @param postDate: the date on which the user is creating the post
    * @param value: the post message
    * @return Integer: the post_id that is being created
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static Integer addPost(String userId, int categoryId,
                                     String title, Timestamp postDate, String value) throws SQLException {
        String query = "INSERT INTO Posts (user_id, category_id, post_title, post_date, post_value)\n" +
                       "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setInt(2, categoryId);
            statement.setString(3, title);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement.setTimestamp(4, postDate, calendar);
            statement.setString(5, value);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that deletes comments
    * @param commentId: the ID of the comment that is to be deleted
    * @return Integer: the number of rows affected
    * @Exception SQL Exception: Failure to connect to database or query fails
    */
    public static Integer deleteComment(Integer commentId) throws SQLException {
        String query = "DELETE FROM Comments\n" +
                       "WHERE comment_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, commentId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that deletes a category
    * @param categoryId: the ID of the category that is to be deleted
    * @return Integer: the number of rows affected
    * @Exception SQL Exception: Failure to connect to database or query fails
    */
    public static Integer deleteCategory(Integer categoryId) throws SQLException {
        String query = "DELETE FROM Categories\n" +
                       "WHERE category_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that deletes a post
     * @param postId: the ID of the post that is to be deleted
     * @return Integer: the number of rows affected
     * @Exception SQL Exception: Failure to connect to database or query fails
     */
    public static Integer deletePost(Integer postId) throws SQLException {
        String query = "DELETE FROM Posts\n" +
                       "WHERE post_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, postId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }
}
