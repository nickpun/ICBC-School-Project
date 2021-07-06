package apiServer.controllers.Module3;

import apiServer.controllers.Module3.JSONRequestSpec.MailRequest;
import apiServer.controllers.Module3.JSONRequestSpec.RequestRequest;
import apiServer.controllers.Module3.JSONRequestSpec.StatusRequest;
import database.ModuleThreeQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@Controller("PostModuleThreeController")
public class PostMappingController {
    @PostMapping("/mail")
    public ResponseEntity<String> addMail(@RequestBody MailRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleThreeQueries.addMail(request.getUserId(),
                                       request.getSender(),
                                       request.getReturnAddress(),
                                       request.getArrivalDate());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/requests")
    public ResponseEntity<String> addMailRequest(@RequestBody RequestRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            int requestId = ModuleThreeQueries.addMailRequest(request.getMailId(),
                                                              request.getUserId(),
                                                              request.getContact(),
                                                              request.getLocation(),
                                                              request.getReqDate(),
                                                              request.getReqType());
            addReqComment(request.getReqComment(), request.getUserId(), requestId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/requests/status")
    public ResponseEntity<String> updateMailRequestAdmin(@RequestBody StatusRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleThreeQueries.updateMailRequestAdmin(request.getRequestId(), request.getStatus());
            addReqComment(request.getReqComment(), request.getUserId(), request.getRequestId());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/requests/update/{requestId}")
    public ResponseEntity<String> updateMailRequestUser(@PathVariable(name = "requestId") Integer requestId,
                                                        @RequestBody RequestRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleThreeQueries.updateMailRequestUser(requestId,
                                                     request.getContact(),
                                                     request.getLocation(),
                                                     request.getReqType(),
                                                     request.getReqDate());
            addReqComment(request.getReqComment(), request.getUserId(), requestId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    private void addReqComment(String reqComment, String userId, Integer requestId) throws SQLException {
        if (!reqComment.equals("")) {
            ModuleThreeQueries.addMailComment(reqComment, userId, requestId);
        }
    }
}
