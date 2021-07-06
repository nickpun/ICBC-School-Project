package apiServer.ModuleA;

import apiServer.TesterHelpers;
import database.data_objects.Employee;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Post extends TesterHelpers {

    @BeforeEach
    public void setUp() {
        clearTesterEmployees();
    }

    @AfterAll
    static void cleanUp() {
        clearTesterEmployees();
    }

    @Test
    public void testAddEmployee() {
        ResponseEntity<String> pResponse = CTRL_MA_P.addEmployee(NICK0_REQUEST);

        Assertions.assertEquals(SUCCESS, pResponse.getBody());
        Assertions.assertEquals(STATUS200, pResponse.getStatusCode());

        List<Employee> employees = CTRL_MA_G.getEmployees().getBody();

        assert employees != null;

        Employee employee = employees.get(employees.size() - 1);

        Assertions.assertEquals(NICK0.getUserId(), employee.getUserId());
    }

    @Test
    public void testAddEmployeeExisting() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);
        List<Employee> employees0 = CTRL_MA_G.getEmployees().getBody();

        ResponseEntity<String> pResponse = CTRL_MA_P.addEmployee(NICK0_REQUEST);

        Assertions.assertEquals(FAILURE, pResponse.getBody());
        Assertions.assertEquals(STATUS500, pResponse.getStatusCode());

        List<Employee> employees1 = CTRL_MA_G.getEmployees().getBody();

        assert employees0 != null;
        assert employees1 != null;
        Assertions.assertEquals(employees0.size(), employees1.size());
    }

    @Test
    public void testAddEmployee2() {
        CTRL_MA_P.addEmployee(NICK0_REQUEST);
        List<Employee> employees0 = CTRL_MA_G.getEmployees().getBody();

        ResponseEntity<String> pResponse = CTRL_MA_P.addEmployee(NICK1_REQUEST);

        Assertions.assertEquals(SUCCESS, pResponse.getBody());
        Assertions.assertEquals(STATUS200, pResponse.getStatusCode());

        List<Employee> employees1 = CTRL_MA_G.getEmployees().getBody();

        assert employees0 != null;
        assert employees1 != null;
        Assertions.assertEquals(employees0.size() + 1, employees1.size());
    }
}
