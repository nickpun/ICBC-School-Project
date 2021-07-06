package apiServer.controllers.ModuleAdmin;

import database.AdministrativeQueries;
import database.data_objects.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@Controller("DeleteModuleAdminController")
public class DeleteMappingController {
    @DeleteMapping("/employees/{userId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "userId") String userId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            List<Employee> employees = AdministrativeQueries.getAllEmployees();
            boolean userExists = false;
            for (Employee employee : employees) {
                if (employee.getUserId() == userId) {
                    userExists = true;
                    break;
                }
            }
            if (userExists) {
                AdministrativeQueries.deleteEmployee(userId);
            } else {
                body = "User DNE";
            }
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }
}
