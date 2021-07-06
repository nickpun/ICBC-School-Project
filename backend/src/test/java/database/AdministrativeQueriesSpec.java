package database;

import database.data_objects.Employee;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class AdministrativeQueriesSpec {

    private static String userId = "100000";
    private static String firstName = "Bob";
    private static String lastName = "The Builder";
    private static String dept = "Software";

    private static String adminUserId = "100001";
    private static String adminFirstName = "Cactus";
    private static String adminLastName = "Law";
    private static String adminDept = "Software";
    private static boolean isAdmin = true;

    private static String systemAdminId = "1337";

    @BeforeClass
    public static void beforeAll() {
        try {
            AdministrativeQueries.addEmployee(userId, firstName, lastName, dept);
            AdministrativeQueries.addEmployee(adminUserId, adminFirstName, adminLastName, adminDept);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testUpdateAdminRole() {
        try {
            int rowsAffected = AdministrativeQueries.updateAdminRole(userId, true);
            Assert.assertEquals(1, rowsAffected);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testIsAdminPass() {
        try {
            Boolean isAdmin = AdministrativeQueries.getIsAdmin(systemAdminId);
            Assert.assertEquals(true, isAdmin);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testIsAdminFail() {
        try {
            Boolean isAdmin = AdministrativeQueries.getIsAdmin(userId);
            Assert.assertEquals(false, isAdmin);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetSystemAdminPass() {
        try {
            Boolean isSystemAdmin = AdministrativeQueries.getIsSystemAdmin(systemAdminId);
            Assert.assertEquals(true, isSystemAdmin);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetSystemAdminFail() {
        try {
            Boolean isSystemAdmin = AdministrativeQueries.getIsSystemAdmin(userId);
            Assert.assertEquals(false, isSystemAdmin);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetAdminList() {
        try {
            int rowsAffected = AdministrativeQueries.updateAdminRole(adminUserId, true);
            Assert.assertEquals(1, rowsAffected);
            List<Employee> adminList = AdministrativeQueries.getAdminList();
            int i = 0;
            while (i < adminList.size()) {
                if (adminList.get(i).getUserId().equals(adminUserId)) {
                    Assert.assertTrue(adminList.get(i).getAdmin());
                    break;
                }
                i++;
            }
            if (i == adminList.size()) {
                Assert.fail();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testIsEmployeeExist() {
        try {
            boolean isEmployeeExist = AdministrativeQueries.isEmployeeExist(userId);
            Assert.assertTrue(isEmployeeExist);
            boolean falseEmployee = AdministrativeQueries.isEmployeeExist("999999999");
            Assert.assertFalse(falseEmployee);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @AfterClass
    public static void afterAll() {
        try {
            int employeeRowsAffected = AdministrativeQueries.deleteEmployee(userId);
            Assert.assertEquals(1, employeeRowsAffected);
            int adminEmployeeRowsAffected = AdministrativeQueries.deleteEmployee(adminUserId);
            Assert.assertEquals(1, adminEmployeeRowsAffected);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

}
