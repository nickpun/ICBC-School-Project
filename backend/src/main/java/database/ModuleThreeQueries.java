package database;

import database.data_objects.*;

import java.sql.*;
import java.util.*;

public class ModuleThreeQueries {

    /*
     * This is the method that returns all non-actioned mail
     * This method should only be called by an admin
     * @return List<Mail>: a list of all non-actioned mail
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Mail> getAllMail() throws SQLException {
        String query = "SELECT mail_id, sender, arrival_date, return_address " +
                       "FROM Mail " +
                       "WHERE was_actioned = ? " +
                       "ORDER BY arrival_date DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, false);
            rs = statement.executeQuery();

            List<Mail> mailList = new ArrayList<>();
            while(rs.next()) {
                int mailId = rs.getInt(1);
                String sender = rs.getString(2);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp arrivalDate = rs.getTimestamp(3, calendar);
                String returnAddress = rs.getString(4);

                Mail mail = new Mail(mailId, sender, arrivalDate, returnAddress);
                mailList.add(mail);
            }

            return mailList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    /*
     * This is the method that returns all non-actioned mail belonging to a specific user
     * @param userId: ID of the user whose mail we want to query
     * @return List<Mail>: a list of all non-actioned mail belonging to the given user
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Mail> getUserMail(String userId) throws SQLException {
        String query = "SELECT mail_id, sender, arrival_date, return_address " +
                       "FROM Mail " +
                       "WHERE was_actioned = ? AND user_id = ? " +
                       "ORDER BY arrival_date DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, false);
            statement.setString(2, userId);
            rs = statement.executeQuery();

            List<Mail> mailList = new ArrayList<>();
            while (rs.next()) {
                int mailId = rs.getInt(1);
                String sender = rs.getString(2);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp arrivalDate = rs.getTimestamp(3, calendar);
                String returnAddress = rs.getString(4);

                Mail mail = new Mail(mailId, sender, arrivalDate, returnAddress);
                mailList.add(mail);
            }

            return mailList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns all MailRequests for a single user
     * @param userId: the id of the user that is retrieving their MailRequests
     * @param requestStatus: the identifying status of the MailRequests to retrieve
     * @return List<MailRequestPreview>: a list of all mail requests
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<MailRequestPreview> getUserMailRequestPreview(String userId, String requestStatus) throws SQLException {
        String query = "SELECT M.request_id, M.mail_id, E.first_name, E.last_name, M.request_date, M.request_type " +
                       "FROM MailRequests M, Employees E " +
                       "WHERE E.user_id = M.user_id\n" +
                       "AND M.user_id = ?\n" +
                       "AND M.request_status = ? " +
                       "ORDER BY request_date ASC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, requestStatus);
            rs = statement.executeQuery();

            List<MailRequestPreview> mailRequestPreviewList = new ArrayList<>();
            while(rs.next()) {
                int requestId = rs.getInt(1);
                int mailId = rs.getInt(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String name = lastName + ", " + firstName;
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp requestDate = rs.getTimestamp(5, calendar);
                String requestType = rs.getString(6);

                MailRequestPreview mailRequestPreview = new MailRequestPreview(requestId, mailId, name, requestDate, requestType);
                mailRequestPreviewList.add(mailRequestPreview);
            }

            return mailRequestPreviewList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns all MailRequests
     * This method should only be called by an admin
     * @param requestStatus: the identifying status of the MailRequests to retrieve
     * @return List<MailRequestPreview>: a list of all mail requests
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<MailRequestPreview> getAllMailRequestsPreview(String requestStatus) throws SQLException {
        String query = "SELECT M.request_id, M.mail_id, E.first_name, E.last_name, M.request_date, M.request_type " +
                       "FROM MailRequests M, Employees E " +
                       "WHERE E.user_id = M.user_id\n" +
                       "AND M.request_status = ? " +
                       "ORDER BY request_date ASC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, requestStatus);
            rs = statement.executeQuery();

            List<MailRequestPreview> mailRequestPreviewList = new ArrayList<>();
            while(rs.next()) {
                int requestId = rs.getInt(1);
                int mailId = rs.getInt(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String name = lastName + ", " + firstName;
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp requestDate = rs.getTimestamp(5, calendar);
                String requestType = rs.getString(6);

                MailRequestPreview mailRequestPreview = new MailRequestPreview(requestId, mailId, name, requestDate, requestType);
                mailRequestPreviewList.add(mailRequestPreview);
            }

            return mailRequestPreviewList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns the details of a single mail request given a requestId
     * @param requestId: ID of the mail request
     * @return MailRequestDetails: an object that contains all necessary details of a mail request
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static MailRequestDetail getMailRequestDetails(int requestId) throws SQLException {
        String query = "SELECT E.first_name, E.last_name, M.contact, M.location, M.request_status, M.request_date, M.request_type " +
                       "FROM MailRequests M, Employees E\n" +
                       "WHERE M.request_id = ?\n" +
                       "AND M.user_id = E.user_id";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, requestId);
            rs = statement.executeQuery();

            rs.next();
            String firstName = rs.getString(1);
            String lastName = rs.getString(2);
            String name = lastName + ", " + firstName;
            String contact = rs.getString(3);
            String location = rs.getString(4);
            String status = rs.getString(5);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            Timestamp requestDate = rs.getTimestamp(6, calendar);
            String requestType = rs.getString(7);

            return new MailRequestDetail(requestId, name, contact, status, location, requestDate, requestType);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    /*
     * This is the method that returns a list of MailComments tied to a specific MailRequest
     * @param requestId: the id of the Request that the MailComments are tied to
     * @return List<MailComment>: a list of MailComments tied to the specified MailRequest
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<MailComment> getMailRequestComments(int requestId) throws SQLException {
        String query = "SELECT MC.comment_value, MC.comment_date, E.last_name, E.first_name\n" +
                       "FROM MailComments MC, Employees E\n" +
                       "WHERE request_id = ?\n" +
                       "AND MC.user_id = E.user_id\n" +
                       "ORDER BY comment_date ASC";
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<MailComment> commentList = new ArrayList<>();
        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, requestId);
            rs = statement.executeQuery();

            while(rs.next()) {
                String commentValue = rs.getString(1);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
                Timestamp commentDate = rs.getTimestamp(2, calendar);
                String lastName = rs.getString(3);
                String firstName = rs.getString(4);
                String name = lastName + ", " + firstName;

                MailComment tempComment = new MailComment(name, commentValue, commentDate);
                commentList.add(tempComment);
            }

            return commentList;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns the userIds and names of all Employees
     * @return Map<Integer, String>: a map of userId keys and the users' names as values
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Map<String, String> getEmployeeNames() throws SQLException {
        String query = "SELECT first_name, last_name, user_id \n" +
                       "FROM Employees\n" +
                       "ORDER BY last_name";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            Map<String, String> userIdNameMap = new LinkedHashMap<>();
            while(rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String userId = rs.getString(3);
                String name = lastName + ", " + firstName;

                userIdNameMap.put(userId, name);
            }

            return userIdNameMap;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that adds a Mail to the database
     * @param userId: the id of the user that the Mail is tied to
     * @param sender: the name of who sent the physical mail
     * @param returnAddress: the address of the person who sent the physical mail
     * @param arrivalDate: the day that the physical mail arrived at the building
     * @return int: the mailId generated by the database when the Mail is successfully added
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static int addMail(String userId, String sender, String returnAddress, Timestamp arrivalDate) throws SQLException {
        String query = "INSERT INTO Mail (user_id, sender, return_address, arrival_date) " +
                       "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, sender);
            statement.setString(3, returnAddress);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement.setTimestamp(4, arrivalDate, calendar);
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
     * This is the method that adds a mail request to the database
     * @param mailId: the ID of the mail that is being requested
     * @param userId: the ID of the user making the request
     * @param contact: the contact of the user
     * @param location: the location of where the use would like to receive the mail
     * @param requestDate: the day on which the request is being made
     * @param requestType: the type of request being made
     * @return int: the ID of the mail request record being created
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static int addMailRequest(int mailId, String userId, String contact, String location,
                                     Timestamp requestDate, String requestType) throws SQLException {
        String query1 = "INSERT INTO MailRequests (mail_id, user_id, contact, location, " +
                            "request_date, request_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE Mail\n" +
                        "SET was_actioned = true\n" +
                        "WHERE mail_id = ?";
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        ResultSet rs1 = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            statement1.setInt(1, mailId);
            statement1.setString(2, userId);
            statement1.setString(3, contact);
            statement1.setString(4, location);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement1.setTimestamp(5, requestDate, calendar);
            statement1.setString(6, requestType);
            statement1.executeUpdate();

            statement2 = connection.prepareStatement(query2);
            statement2.setInt(1, mailId);
            statement2.executeUpdate();

            rs1 = statement1.getGeneratedKeys();
            rs1.next();
            return rs1.getInt(1);

        } finally {
            try { if (rs1 != null) rs1.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement1 != null) statement1.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement2 != null) statement2.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    /*
     * This is the method that updates a MailRequest for admins
     * @param requestId: the id of the MailRequest being updated
     * @param requestStatus: the new status of the MailRequest being changed
     * @return Integer: the number of rows affected by the query (1 if the UPDATE succeeds)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer updateMailRequestAdmin(int requestId, String requestStatus) throws SQLException {
        String query = "UPDATE MailRequests\n" +
                       "SET request_status = ?\n" +
                       "WHERE request_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, requestStatus);
            statement.setInt(2, requestId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that updates a MailRequest for the user that made the MailRequest
     * @param requestId: the id of the MailRequest being updated
     * @param contact: the string value of how / where to contact the user
     * @param location: the location of where the user wants the request action to take place
     * @param requestType: the type of request that the user wants ("hold", "forward", "open and assist")
     * @return Integer: the number of rows affected by the query (1 if the UPDATE succeeds)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer updateMailRequestUser(int requestId, String contact, String location, String requestType, Timestamp requestDate) throws SQLException {
        String query = "UPDATE MailRequests\n" +
                       "SET contact = ?, location = ?, request_type = ?, request_date = ?\n" +
                       "WHERE request_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, contact);
            statement.setString(2, location);
            statement.setString(3, requestType);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement.setTimestamp(4, requestDate, calendar);
            statement.setInt(5, requestId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that adds a MailComment to a MailRequest
     * @param mailComment: the comment inputted by the user
     * @param userId: the id of the user who made the comment
     * @param requestId: the id of the MailRequest being commented on
     * @return Integer: the MailComment id generated by the database if the ADD succeeds
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer addMailComment(String mailComment, String userId, int requestId) throws SQLException {
        String query = "INSERT INTO MailComments (comment_value, comment_date, user_id, request_id)\n" +
                       "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, mailComment);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver"));
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()), calendar);
            statement.setString(3, userId);
            statement.setInt(4, requestId);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that deletes a Mail from the database
     * @param mailId: the id of the Mail being deleted
     * @return Integer: the number of rows affected by the query (1 if the DELETE succeeds)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer deleteMail(int mailId) throws SQLException {
        String query = "DELETE FROM Mail\n" +
                       "WHERE mail_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, mailId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that deletes a MailRequest from the database. If the delete happens when the status of the request
     * is not "closed", this method will set its respective Mail's was_actioned column to 0.
     * @param mailId: the id of the MailRequest being deleted
     * @return Integer: the number of rows affected by the query (1 if the DELETE succeeds)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer deleteMailRequest(int requestId) throws SQLException {
        String query1 = "SELECT mail_id, request_status\n" +
                        "FROM MailRequests\n" +
                        "WHERE request_id = ?";
        String query2 = "UPDATE Mail\n" +
                        "SET was_actioned = 0\n" +
                        "WHERE mail_id = ?";
        String query3 = "DELETE FROM MailRequests\n" +
                        "WHERE request_id = ?";
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;

        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement1 = connection.prepareStatement(query1);
            statement1.setInt(1, requestId);
            rs = statement1.executeQuery();

            rs.next();

            int mailId = rs.getInt(1);
            String requestStatus = rs.getString(2);

            if (!requestStatus.equals("closed")) {
                statement2 = connection.prepareStatement(query2);
                statement2.setInt(1, mailId);
                statement2.executeUpdate();
            }

            statement3 = connection.prepareStatement(query3);
            statement3.setInt(1, requestId);
            return statement3.executeUpdate();

        } finally {
            try { if (statement1 != null) statement1.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement2 != null) statement2.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (statement3 != null) statement3.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

}
