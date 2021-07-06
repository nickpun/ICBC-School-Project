package database;

import database.data_objects.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministrativeQueries {
    /*
     * Method to add an Employee to the database
     * @param userId: the id of the user returned from AD
     * @param firstName: the first name of the user
     * @param lastName: the last name of the user
     * @param dept: the department name of the user
     * @param isAdmin: whether the user has admin rights or not
     * @return Integer: the number of rows affected (1 if the Employee is created)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer addEmployee(String userId, String firstName, String lastName, String dept) throws SQLException {
        String query = "INSERT INTO Employees (user_id, first_name, last_name, dept)\n" +
                       "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, dept);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that will delete an Employee
     * @param user: the ID of the Employee that is to be deleted
     * @return int: The number of rows affected
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static int deleteEmployee(String userId) throws SQLException {
        String query = "DELETE FROM Employees\n" +
                       "WHERE user_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns all Employees in the database
     * @return List<Employee>: A list of Employees in the database
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static List<Employee> getAllEmployees() throws SQLException {
        String query = "SELECT * FROM Employees ORDER BY last_name";
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<Employee> employeeList = new ArrayList<>();
        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            while(rs.next()) {
                String userId = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String dept = rs.getString(4);
                Boolean isAdmin = rs.getBoolean(5);

                Employee tempEmployee = new Employee(userId, firstName, lastName, dept, isAdmin);
                employeeList.add(tempEmployee);
            }
            return employeeList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that checks whether a user is an admin or not
     * @param userId: the id of the user being checked
     * @return Boolean: true if the user is an admin, false if not
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static Boolean getIsAdmin(String userId) throws SQLException {
        String query = "SELECT is_admin\n" +
                       "FROM Employees\n" +
                       "WHERE user_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            rs = statement.executeQuery();

            rs.next();
            return rs.getBoolean(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that checks whether a user is a system admin or not
     * @param userId: the id of the user being checked
     * @return Boolean: true if the user is a system admin, false if not
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static Boolean getIsSystemAdmin(String userId) throws SQLException {
        String query = "SELECT is_system_admin\n" +
                       "FROM Employees\n" +
                       "WHERE user_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            rs = statement.executeQuery();

            rs.next();
            return rs.getBoolean(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that updates the admin role of a certain user
     * @param userId: the id of the user whose admin role is being updated
     * @param isAdmin: the updated admin role value; true if is an admin, false if not
     * @return Integer: the number of rows affected
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static Integer updateAdminRole(String userId, boolean isAdmin) throws SQLException {
        String query = "UPDATE Employees " +
                       "SET is_admin = ? " +
                       "WHERE user_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, isAdmin);
            statement.setString(2, userId);
            return statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns a list of users that are admins and that are not system admins
     * @return List<Employee>: a list of Employees
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static List<Employee> getAdminList() throws SQLException {
        String query = "SELECT user_id, first_name, last_name, dept, is_admin " +
                       "FROM Employees " +
                       "WHERE is_admin = ? " +
                       "AND is_system_admin != 1";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, true);
            rs = statement.executeQuery();

            List<Employee> adminList = new ArrayList<>();
            while (rs.next()) {
                String userId = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String dept = rs.getString(4);
                boolean isAdmin = rs.getBoolean(5);
                Employee employee = new Employee(userId, firstName, lastName, dept, isAdmin);
                adminList.add(employee);
            }
            return adminList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that checks if a user exists within the database
     * @param userId: the id of the user being checked
     * @return List<Employee>: a list of Employees
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static boolean isEmployeeExist(String userId) throws SQLException {
        String query = "SELECT user_id " +
                "FROM Employees " +
                "WHERE user_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            rs = statement.executeQuery();

            return rs.isBeforeFirst();

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

}
