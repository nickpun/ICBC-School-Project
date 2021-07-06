package apiServer.controllers.Module3;

import database.ModuleThreeQueries;
import database.data_objects.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller("GetModuleThreeController")
public class GetMappingController {
    @GetMapping("/mail")
    public ResponseEntity<List<Mail>> getAllMail() {
        try {
            List<Mail> data = ModuleThreeQueries.getAllMail();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mail/{userId}")
    public ResponseEntity<Map<String, Object>> getUserMailAndRequests(@PathVariable(name = "userId") String userId) {
        try {
            String keyMail = "mail";
            String keyNew = "newRequest";
            String keyWIP = "wip";
            String keyClosed = "closed";

            List<Mail> valMail = ModuleThreeQueries.getUserMail(userId);
            List<MailRequestPreview> valNew = ModuleThreeQueries.getUserMailRequestPreview(userId, keyNew);
            List<MailRequestPreview> valWIP = ModuleThreeQueries.getUserMailRequestPreview(userId, keyWIP);
            List<MailRequestPreview> valClosed = ModuleThreeQueries.getUserMailRequestPreview(userId, keyClosed);

            Map<String, Object> map = new HashMap<>();
            map.put(keyMail, valMail);
            map.put(keyNew, valNew);
            map.put(keyWIP, valWIP);
            map.put(keyClosed, valClosed);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<Map<String, Object>> getAllMailRequestsPreview() {
        try {
            String keyNew = "newRequest";
            String keyWIP = "wip";
            String keyClosed = "closed";

            List<MailRequestPreview> valNew = ModuleThreeQueries.getAllMailRequestsPreview(keyNew);
            List<MailRequestPreview> valWIP = ModuleThreeQueries.getAllMailRequestsPreview(keyWIP);
            List<MailRequestPreview> valClosed = ModuleThreeQueries.getAllMailRequestsPreview(keyClosed);

            Map<String, Object> map = new HashMap<>();
            map.put(keyNew, valNew);
            map.put(keyWIP, valWIP);
            map.put(keyClosed, valClosed);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requests/details/{requestId}")
    public ResponseEntity<Map<String, Object>> getMailRequestDetails(@PathVariable(name = "requestId") Integer requestId) {
        try {
            MailRequestDetail details = ModuleThreeQueries.getMailRequestDetails(requestId);
            List<MailComment> comments = ModuleThreeQueries.getMailRequestComments(requestId);

            Map<String, Object> map = new HashMap<>();
            map.put("details", details);
            map.put("comments", comments);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mail/employees")
    public ResponseEntity< List<EmployeeName> > getEmployeeNames() {
        try {
            List<EmployeeName> data = new ArrayList<>();
            Map<String, String> employeeNamesMap = ModuleThreeQueries.getEmployeeNames();
            for (Map.Entry<String, String> employeeEntry : employeeNamesMap.entrySet()) {
                EmployeeName tempEmployeeName = new EmployeeName(employeeEntry.getKey(), employeeEntry.getValue());
                data.add(tempEmployeeName);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
