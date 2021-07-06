package apiServer.ModuleA;

import apiServer.TesterHelpers;
import database.data_objects.Employee;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Get extends TesterHelpers {

    @BeforeEach
    public void setUp() {
        clearTesterEmployees();
    }

    @AfterAll
    static void cleanUp() {
        clearTesterEmployees();
    }

    @Test
    public void testGetEmployees() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);

        ResponseEntity<List<Employee>> gResponse = CTRL_MA_G.getEmployees();
        List<Employee> employees = gResponse.getBody();

        assert employees != null;

        Employee employee = employees.get(employees.size() - 1);

        Assertions.assertEquals(NICK0.getUserId(), employee.getUserId());
        Assertions.assertEquals(STATUS200, gResponse.getStatusCode());
    }

    @Test
    public void testGetEmployees2() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);
        CTRL_MA_P.addEmployee(NICK1_REQUEST);

        ResponseEntity<List<Employee>> gResponse = CTRL_MA_G.getEmployees();
        List<Employee> employees = gResponse.getBody();

        assert employees != null;

        Employee employee0 = employees.get(employees.size() - 2);
        Employee employee1 = employees.get(employees.size() - 1);

        Assertions.assertEquals(NICK0.getUserId(), employee0.getUserId());
        Assertions.assertEquals(NICK1.getUserId(), employee1.getUserId());
        Assertions.assertEquals(STATUS200, gResponse.getStatusCode());
    }
}
