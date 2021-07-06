package apiServer.ModuleA;

import apiServer.TesterHelpers;
import database.data_objects.Employee;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Delete extends TesterHelpers {

    @AfterAll
    static void cleanUp() {
        clearTesterEmployees();
    }

    @Test
    public void testDeleteEmployee() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);
        List<Employee> employees0 = CTRL_MA_G.getEmployees().getBody();

        ResponseEntity<String> dResponse = CTRL_MA_D.deleteEmployee(NICK0.getUserId());

        Assertions.assertEquals(SUCCESS, dResponse.getBody());
        Assertions.assertEquals(STATUS200, dResponse.getStatusCode());

        List<Employee> employees1 = CTRL_MA_G.getEmployees().getBody();

        assert employees0 != null;
        assert employees1 != null;

        Assertions.assertEquals(employees0.size() - 1, employees1.size());
    }

    @Test
    public void testDeleteEmployeeDNE() {
        CTRL_MA_D.deleteEmployee(NICK0.getUserId());
        List<Employee> employees0 = CTRL_MA_G.getEmployees().getBody();

        ResponseEntity<String> dResponse = CTRL_MA_D.deleteEmployee(NICK0.getUserId());

        Assertions.assertEquals(USER_DNE, dResponse.getBody());
        Assertions.assertEquals(STATUS200, dResponse.getStatusCode());

        List<Employee> employees1 = CTRL_MA_G.getEmployees().getBody();

        assert employees0 != null;
        assert employees1 != null;

        Assertions.assertEquals(employees0.size(), employees1.size());
    }
}
