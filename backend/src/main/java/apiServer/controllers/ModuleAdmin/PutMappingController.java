package apiServer.controllers.ModuleAdmin;

import apiServer.controllers.ModuleAdmin.JSONRequestSpec.AdminRequest;
import apiServer.controllers.ModuleAdmin.JSONRequestSpec.EmployeeRequest;
import database.AdministrativeQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@Controller("PutModuleAdminController")
public class PutMappingController {
    @PutMapping("/employees")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            AdministrativeQueries.addEmployee(request.getUserId(),
                                              request.getFirstName(),
                                              request.getLastName(),
                                              request.getDept());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PutMapping("/employees/admin")
    public ResponseEntity<Integer> updateAdminRole(@RequestBody AdminRequest request) {
        try {
            Integer data = AdministrativeQueries.updateAdminRole(request.getUserId(), request.getIsAdmin());
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
