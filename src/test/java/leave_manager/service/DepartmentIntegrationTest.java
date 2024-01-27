package leave_manager.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.model.Department;
import com.example.leave_manager.repository.DepartmentRepo;
import com.example.leave_manager.repository.UsersRepo;
import com.example.leave_manager.LeaveManagerApplication;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeaveManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentIntegrationTest {

    private final Department firstDepartment = Department.builder()
            .departmentName("Management")
            .build();
    private final Department secondDepartment = Department.builder()
            .departmentName("IT")
            .build();

    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private UsersRepo usersRepo;
    private String baseUrl = "http://localhost:";

    @BeforeAll
    public void init() {
        testRestTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setup() {
        baseUrl = baseUrl.concat(String.valueOf(port)).concat("/api/department");
        departmentRepo.save(firstDepartment);
        departmentRepo.save(secondDepartment);
    }

    @AfterEach
    public void delete() {
        departmentRepo.deleteById(firstDepartment.getDepartmentId());
        departmentRepo.deleteById(secondDepartment.getDepartmentId());
    }

    @Test
    public void testCreateDepartment() {
        DepartmentDto departmentDto = new DepartmentDto().builder()
                .departmentName("New department")
                .build();
        long size = departmentRepo.count();
        String response = testRestTemplate.withBasicAuth("Will", "2222")
                .postForObject(baseUrl + "/add", departmentDto, String.class);
        assertNotNull(response);
        assertEquals("Department added!", response);
        assertEquals(size + 1, departmentRepo.count());
    }

    @Test
    public void testGetAllDepartments() throws IOException {
        String result = testRestTemplate.withBasicAuth("Will", "2222")
                .getForObject(baseUrl + "/getAll", String.class);
        List<DepartmentDto> resultList = new ObjectMapper().readValue(
                result, new TypeReference<List<DepartmentDto>>(){
                });
        assertEquals("IT", resultList.get(15).getDepartmentName());
        assertEquals(16, resultList.size());
    }

    @Test
    public void testGetById() throws IOException {
        String result = testRestTemplate.withBasicAuth("Will", "2222")
                .getForObject(baseUrl + "/getById/{id}", String.class, firstDepartment.getDepartmentId());
        List<DepartmentDto> resultList = new ObjectMapper().readValue(
                result, new TypeReference<List<DepartmentDto>>() {
                });
        assertEquals("Management", resultList.get(0).getDepartmentName());
    }

    @Test
    public void testUpdate() {
        DepartmentDto newDepartmentDto = new DepartmentDto().builder()
                .departmentName("Updated department")
                .build();
        testRestTemplate.withBasicAuth("Will", "2222")
                .put(baseUrl + "/update/{id}", newDepartmentDto, secondDepartment.getDepartmentId());
        Optional<Department> updatedDepartment = departmentRepo.findById(secondDepartment.getDepartmentId());
        updatedDepartment.ifPresent(department -> assertEquals("Updated department", department.getDepartmentName()));
    }

    @Test
    public void testDeleteById() {
        testRestTemplate.withBasicAuth("Will", "2222")
                .delete(baseUrl + "/deleteById/{id}", secondDepartment.getDepartmentId());
        assertEquals(3, usersRepo.findAll().size());
    }

}
