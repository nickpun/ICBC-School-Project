package apiServer;

import apiServer.controllers.Module3.JSONRequestSpec.MailRequest;
import apiServer.controllers.Module3.JSONRequestSpec.RequestRequest;
import apiServer.controllers.Module3.JSONRequestSpec.StatusRequest;
import apiServer.controllers.ModuleAdmin.JSONRequestSpec.EmployeeRequest;
import database.data_objects.Employee;
import database.data_objects.Mail;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class TesterHelpers {
    protected static final String SUCCESS = "Success";
    protected static final String FAILURE = "Failure";
    protected static final String USER_DNE = "User DNE";

    protected static final String HOLD = "hold";
    protected static final String FWD = "forward";
    protected static final String OAA = "open and assist";

    protected static final String NEWREQ = "newRequest";
    protected static final String WIP = "wip";
    protected static final String CLSOED = "closed";

    protected static final HttpStatus STATUS200 = HttpStatus.OK;
    protected static final HttpStatus STATUS500 = HttpStatus.INTERNAL_SERVER_ERROR;

    protected static final Employee NICK0 = new Employee(
            "278721673",
            "Nicholas",
            "Pun0",
            "CPSC & PHYS",
            true
    );
    protected static final Employee NICK1 = new Employee(
            "278721674",
            "Nicholas",
            "Pun1",
            "CPSC & PHYS",
            true
    );

    protected static final EmployeeRequest NICK0_REQUEST = new EmployeeRequest(
            NICK0.getUserId(),
            NICK0.getFirstName(),
            NICK0.getLastName(),
            NICK0.getDept()
    );
    protected static final EmployeeRequest NICK1_REQUEST = new EmployeeRequest(
            NICK1.getUserId(),
            NICK1.getFirstName(),
            NICK1.getLastName(),
            NICK1.getDept()
    );

    protected static final MailRequest NICK0_MAIL0_REQUEST = new MailRequest(
            NICK0.getUserId(),
            "Nicholas Pun1",
            DateTimeUtil.getTimestamp("2022-01-01T00:00:00Z"),
            "Nicholas Pun1's address"
    );

    protected static final MailRequest NICK0_MAIL1_REQUEST = new MailRequest(
            NICK0.getUserId(),
            "Nicholas Pun1",
            DateTimeUtil.getTimestamp("2022-01-01T00:00:00Z"),
            "Nicholas Pun1's address"
    );

    protected static final apiServer.controllers.Module1.DeleteMappingController CTRL_M1_D =
            new apiServer.controllers.Module1.DeleteMappingController();
    protected static final apiServer.controllers.Module1.GetMappingController CTRL_M1_G =
            new apiServer.controllers.Module1.GetMappingController();
    protected static final apiServer.controllers.Module1.PostMappingController CTRL_M1_P =
            new apiServer.controllers.Module1.PostMappingController();
    protected static final apiServer.controllers.Module2.DeleteMappingController CTRL_M2_D =
            new apiServer.controllers.Module2.DeleteMappingController();
    protected static final apiServer.controllers.Module2.GetMappingController CTRL_M2_G =
            new apiServer.controllers.Module2.GetMappingController();
    protected static final apiServer.controllers.Module2.PostMappingController CTRL_M2_P =
            new apiServer.controllers.Module2.PostMappingController();
    protected static final apiServer.controllers.Module3.DeleteMappingController CTRL_M3_D =
            new apiServer.controllers.Module3.DeleteMappingController();
    protected static final apiServer.controllers.Module3.GetMappingController CTRL_M3_G =
            new apiServer.controllers.Module3.GetMappingController();
    protected static final apiServer.controllers.Module3.PostMappingController CTRL_M3_P =
            new apiServer.controllers.Module3.PostMappingController();
    protected static final apiServer.controllers.ModuleAdmin.DeleteMappingController CTRL_MA_D =
            new apiServer.controllers.ModuleAdmin.DeleteMappingController();
    protected static final apiServer.controllers.ModuleAdmin.GetMappingController CTRL_MA_G =
            new apiServer.controllers.ModuleAdmin.GetMappingController();
    protected static final apiServer.controllers.ModuleAdmin.PutMappingController CTRL_MA_P =
            new apiServer.controllers.ModuleAdmin.PutMappingController();

    protected static void addTesterEmployees() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);
        CTRL_MA_P.addEmployee(NICK1_REQUEST);
    }

    protected static void clearTesterEmployees() {
        CTRL_MA_D.deleteEmployee(NICK0.getUserId());
        CTRL_MA_D.deleteEmployee(NICK1.getUserId());
    }

    protected static RequestRequest getNICK0RequestRequest(int mailId) {
        return new RequestRequest(
                mailId,
                NICK0.getUserId(),
                "Nicholas Pun0's contact",
                "Nicholas Pun0's location",
                DateTimeUtil.getTimestamp("2022-01-01T00:00:00Z"),
                HOLD,
                ""
        );
    }

    protected static int addMailGetId(MailRequest mailRequest) {
        CTRL_M3_P.addMail(mailRequest);
        Map<String, Object> map = CTRL_M3_G.getUserMailAndRequests(NICK0.getUserId()).getBody();
        assert map != null;
        List<Mail> mails = (List<Mail>) map.get("mail");
        assert mails != null;
        Mail mail = mails.get(mails.size() - 1);
        return mail.getMailId();
    }

    protected static StatusRequest getNICK0StatusRequest(int requestId) {
        return new StatusRequest(
                requestId,
                NICK0.getUserId(),
                WIP,
                ""
        );
    }
}
