package apiServer.controllers.ModuleAdmin;

import database.AdministrativeQueries;
import database.data_objects.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@Controller("GetModuleAdminController")
public class GetMappingController {
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        try {
            List<Employee> data = AdministrativeQueries.getAllEmployees();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{userId}")
    public ResponseEntity<Boolean> isEmployee(@PathVariable(name = "userId") String userId) {
        try {
            boolean data = AdministrativeQueries.isEmployeeExist(userId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Employee>> getAdmins() {
        try {
            List<Employee> data = AdministrativeQueries.getAdminList();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/isadmin/{userId}")
    public ResponseEntity<Boolean> checkIfAdmin(@PathVariable(name = "userId") String userId) {
        try {
            Boolean data = AdministrativeQueries.getIsAdmin(userId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/issysadmin/{userId}")
    public ResponseEntity<Boolean> checkIfSysadmin(@PathVariable(name = "userId") String userId) {
        try {
            Boolean data = AdministrativeQueries.getIsSystemAdmin(userId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
