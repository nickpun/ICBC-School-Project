package apiServer.Module3;

import apiServer.TesterHelpers;
import apiServer.controllers.Module3.JSONRequestSpec.RequestRequest;
import apiServer.controllers.Module3.JSONRequestSpec.StatusRequest;
import database.data_objects.Mail;
import database.data_objects.MailComment;
import database.data_objects.MailRequestDetail;
import database.data_objects.MailRequestPreview;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class Post extends TesterHelpers {

    @BeforeEach
    public void setUp() {
        addTesterEmployees();
    }

    @AfterAll
    static void cleanUp() {
        clearTesterEmployees();
    }

    @Test
    public void testAddMail() {
        ResponseEntity<String> pResponse = CTRL_M3_P.addMail(NICK0_MAIL0_REQUEST);

        Assertions.assertEquals(SUCCESS, pResponse.getBody());
        Assertions.assertEquals(STATUS200, pResponse.getStatusCode());

        Map<String, Object> map0 = CTRL_M3_G.getUserMailAndRequests(NICK0.getUserId()).getBody();
        assert map0 != null;
        List<Mail> mails0 = (List<Mail>) map0.get("mail");

        CTRL_M3_P.addMail(NICK0_MAIL0_REQUEST);

        Map<String, Object> map1 = CTRL_M3_G.getUserMailAndRequests(NICK0.getUserId()).getBody();
        assert map1 != null;
        List<Mail> mails1 = (List<Mail>) map1.get("mail");

        assert mails0 != null;
        assert mails1 != null;

        Mail mail0 = mails0.get(mails0.size() - 1);
        Mail mail1 = mails1.get(mails1.size() - 1);
        int mailId0 = mail0.getMailId();
        int mailId1 = mail1.getMailId();

        Assertions.assertEquals(mailId0 + 1, mailId1);

        CTRL_M3_D.deleteMail(mailId0);
        CTRL_M3_D.deleteMail(mailId1);
    }

    @Test
    public void testAddMailRequest() {
        int mailId = addMailGetId(NICK0_MAIL0_REQUEST);
        RequestRequest nick0RequestRequest = getNICK0RequestRequest(mailId);

        ResponseEntity<String> pResponse = CTRL_M3_P.addMailRequest(nick0RequestRequest);

        Assertions.assertEquals(SUCCESS, pResponse.getBody());
        Assertions.assertEquals(STATUS200, pResponse.getStatusCode());

        mailId = addMailGetId(NICK0_MAIL0_REQUEST);
        nick0RequestRequest = getNICK0RequestRequest(mailId);

        CTRL_M3_P.addMailRequest(nick0RequestRequest);

        Map<String, Object> map = CTRL_M3_G.getUserMailAndRequests(NICK0.getUserId()).getBody();
        assert map != null;
        List<MailRequestPreview> reqs = (List<MailRequestPreview>) map.get("newRequest");

        Assertions.assertEquals(2, reqs.size());

        MailRequestPreview req0 = reqs.get(0);
        MailRequestPreview req1 = reqs.get(1);

        Assertions.assertEquals(mailId, req1.getMailId());
        Assertions.assertEquals(req0.getRequestId() + 1, req1.getRequestId());
        Assertions.assertEquals(NICK0.getLastName() + ", " + NICK0.getFirstName(), req1.getName());
        Assertions.assertEquals(nick0RequestRequest.getReqDate(), req1.getRequestDate());
        Assertions.assertEquals(nick0RequestRequest.getReqType(), req1.getRequestType());
    }

    @Test
    public void testUpdateMailRequestAdmin() {
        int mailId = addMailGetId(NICK0_MAIL0_REQUEST);
        RequestRequest nick0RequestRequest = getNICK0RequestRequest(mailId);
        CTRL_M3_P.addMailRequest(nick0RequestRequest);

        Map<String, Object> map = CTRL_M3_G.getUserMailAndRequests(NICK0.getUserId()).getBody();
        assert map != null;
        List<MailRequestPreview> reqs = (List<MailRequestPreview>) map.get("newRequest");
        MailRequestPreview req = reqs.get(0);
        int requestId = req.getRequestId();
        StatusRequest nick0StatusRequest = getNICK0StatusRequest(requestId);

        ResponseEntity<String> pResponse = CTRL_M3_P.updateMailRequestAdmin(nick0StatusRequest);

        Assertions.assertEquals(SUCCESS, pResponse.getBody());
        Assertions.assertEquals(STATUS200, pResponse.getStatusCode());

        Map<String, Object> reqDetails = CTRL_M3_G.getMailRequestDetails(requestId).getBody();
        assert reqDetails != null;
        MailRequestDetail details = (MailRequestDetail) reqDetails.get("details");
        List<MailComment> comments = (List<MailComment>) reqDetails.get("comments");

        Assertions.assertEquals(WIP, details.getStatus());
        Assertions.assertEquals(0, comments.size());
    }
}
