package database;

import database.data_objects.MailComment;
import database.data_objects.MailRequestDetail;
import database.data_objects.Mail;
import database.data_objects.MailRequestDetail;
import database.data_objects.MailRequestPreview;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ModuleThreeQueriesSpec {

    private static String userId = "100000";
    private static String firstName = "Bob";
    private static String lastName = "The Builder";
    private static String dept = "Software";

    private static String userId2 = "100001";
    private static String firstName2 = "Jesus";
    private static String lastName2 = "Christ";
    private static String dept2 = "Religion";
    private static boolean isAdmin2 = true;

    private static int mailId;
    private static String sender = "Thomas";
    private static String returnAddress = "1111 Park Lane";
    private static Timestamp arrivalDate = new Timestamp(32510781572000L);

    private static int mailId2;
    private static String sender2 = "Hank Hill";
    private static String returnAddress2 = "1001 Hill Street";
    private static Timestamp arrivalDate2 = new Timestamp(100000000);

    private static int mailRequestId;
    private static String contact = "999-999-9999";
    private static String location = "Somewhere";
    private static Timestamp requestDate = new Timestamp(100000000);
    private static String requestType = "hold";

    @BeforeClass
    public static void beforeAll() {
        try {
            int rowsAffected = AdministrativeQueries.addEmployee(userId, firstName, lastName, dept);
            Assert.assertEquals(1, rowsAffected);
            int rowsAffected2 = AdministrativeQueries.addEmployee(userId2, firstName2, lastName2, dept2);
            Assert.assertEquals(1, rowsAffected2);
            mailId = ModuleThreeQueries.addMail(userId, sender, returnAddress, arrivalDate);
            mailId2 = ModuleThreeQueries.addMail(userId, sender2, returnAddress2, arrivalDate2);
            mailRequestId = ModuleThreeQueries.addMailRequest(mailId2, userId, contact,
                    location, requestDate, requestType);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetAllMail() {
        try {
            List<Mail> allMail = ModuleThreeQueries.getAllMail();
            Mail mail = allMail.get(0);
            Assert.assertEquals(mailId, mail.getMailId());
            Assert.assertEquals(sender, mail.getSender());
            Assert.assertEquals(returnAddress, mail.getReturnAddress());
            Assert.assertEquals(arrivalDate.getTime(),
                    mail.getArrivalDate().getTime());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetUserMail() {
        try {
            List<Mail> allUserMail = ModuleThreeQueries.getUserMail(userId);
            Mail mail = allUserMail.get(0);
            Assert.assertEquals(mailId, mail.getMailId());
            Assert.assertEquals(sender, mail.getSender());
            Assert.assertEquals(returnAddress, mail.getReturnAddress());
            Assert.assertEquals(arrivalDate.getTime(),
                    mail.getArrivalDate().getTime());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetUserMailRequestPreview() {
        try {
            List<MailRequestPreview> mailRequestPreviewList =
                    ModuleThreeQueries.getUserMailRequestPreview(userId, "newRequest");
            MailRequestPreview mailRequestPreview = mailRequestPreviewList.get(mailRequestPreviewList.size() - 1);
            Assert.assertEquals(mailRequestId, mailRequestPreview.getRequestId());
            Assert.assertEquals(mailId2, mailRequestPreview.getMailId());
            Assert.assertEquals(lastName + ", " + firstName, mailRequestPreview.getName());
            Assert.assertEquals(requestDate.getTime(),
                    mailRequestPreview.getRequestDate().getTime());
            Assert.assertEquals(requestType, mailRequestPreview.getRequestType());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetAllMailRequestPreview() {
        try {
            List<MailRequestPreview> mailRequestPreviewList =
                    ModuleThreeQueries.getAllMailRequestsPreview("newRequest");
            MailRequestPreview mailRequestPreview = mailRequestPreviewList.get(0);
            Assert.assertEquals(mailRequestId, mailRequestPreview.getRequestId());
            Assert.assertEquals(mailId2, mailRequestPreview.getMailId());
            Assert.assertEquals(lastName + ", " + firstName, mailRequestPreview.getName());
            Assert.assertEquals(requestDate.getTime(),
                    mailRequestPreview.getRequestDate().getTime());
            Assert.assertEquals(requestType, mailRequestPreview.getRequestType());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetEmployeeNames() {
        try {
            Map<String, String> employeeNames = ModuleThreeQueries.getEmployeeNames();
            if (employeeNames.containsKey(userId)) {
                Assert.assertTrue(true);
                String name = lastName + ", " + firstName;
                Assert.assertEquals(name, employeeNames.get(userId));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testUpdateMailRequestAdmin() {
        try {
            int rowsAffected = ModuleThreeQueries.updateMailRequestAdmin(mailRequestId, "work in progress");
            MailRequestDetail mailRequestDetail = ModuleThreeQueries.getMailRequestDetails(mailRequestId);


            Assert.assertEquals(1, rowsAffected);
            Assert.assertEquals(mailRequestId, mailRequestDetail.getRequestId());
            Assert.assertEquals("work in progress", mailRequestDetail.getStatus());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testUpdateMailRequestUser() {
        try {
            String newContact = "911";
            String newLocation = "some police department";
            String newRequestType = "open and assist";
            Timestamp newRequestDate = new Timestamp(1613865700000L);

            int rowsAffected = ModuleThreeQueries.updateMailRequestUser(mailRequestId, newContact, newLocation, newRequestType, newRequestDate);
            Assert.assertEquals(1, rowsAffected);
            MailRequestDetail mailRequestDetail = ModuleThreeQueries.getMailRequestDetails(mailRequestId);
            Assert.assertEquals(newContact, mailRequestDetail.getContact());
            Assert.assertEquals(newLocation, mailRequestDetail.getLocation());
            Assert.assertEquals(newRequestType, mailRequestDetail.getRequestType());
            Assert.assertEquals(newRequestDate.getTime(),
                    mailRequestDetail.getRequestDate().getTime());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testAddMailComment() {
        try {
            String mailCommentValue1 = "why hello there";
            String mailCommentValue2 = "general kenobi...";

            ModuleThreeQueries.addMailComment(mailCommentValue1, userId, mailRequestId);
            ModuleThreeQueries.addMailComment(mailCommentValue2, userId2, mailRequestId);

            List<MailComment> mailCommentList = ModuleThreeQueries.getMailRequestComments(mailRequestId);
            Assert.assertEquals(lastName + ", " + firstName, mailCommentList.get(0).getName());
            Assert.assertEquals(mailCommentValue1, mailCommentList.get(0).getCommentValue());

            Assert.assertEquals(lastName2 + ", " + firstName2, mailCommentList.get(1).getName());
            Assert.assertEquals(mailCommentValue2, mailCommentList.get(1).getCommentValue());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @AfterClass
    public static void afterAll() {
        try {
            int mailRequestRowsAffected = ModuleThreeQueries.deleteMailRequest(mailRequestId);
            Assert.assertEquals(1, mailRequestRowsAffected);
            int mailRowsAffected = ModuleThreeQueries.deleteMail(mailId);
            Assert.assertEquals(1, mailRowsAffected);
            int mailRowsAffected2 = ModuleThreeQueries.deleteMail(mailId2);
            Assert.assertEquals(1, mailRowsAffected2);
            int userRowsAffected = AdministrativeQueries.deleteEmployee(userId);
            Assert.assertEquals(1, userRowsAffected);
            int userRowsAffected2 = AdministrativeQueries.deleteEmployee(userId2);
            Assert.assertEquals(1, userRowsAffected2);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

}
