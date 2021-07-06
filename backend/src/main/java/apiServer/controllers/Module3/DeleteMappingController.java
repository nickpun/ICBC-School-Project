package apiServer.controllers.Module3;

import database.ModuleThreeQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@Controller("DeleteModuleThreeController")
public class DeleteMappingController {

    @DeleteMapping("/mail/{mailId}")
    public ResponseEntity<String> deleteMail(@PathVariable(name = "mailId") Integer mailId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleThreeQueries.deleteMail(mailId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<String> deleteMailRequest(@PathVariable(name = "requestId") Integer requestId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleThreeQueries.deleteMailRequest(requestId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }
}
