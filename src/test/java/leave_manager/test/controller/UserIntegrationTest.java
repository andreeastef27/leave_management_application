package leave_manager.test.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.example.leave_manager.LeaveManagerApplication;
import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.model.Department;
import com.example.leave_manager.model.User;
import com.example.leave_manager.repository.DepartmentRepo;
import com.example.leave_manager.repository.UsersRepo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeaveManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    private final User firstUser = User.builder()
            .departmentId(2)
            .name("Bill")
            .surname("Smith")
            .marca("1111")
            .role("USER")
            .build();
    private final User secondUser = User.builder()
            .departmentId(2)
            .name("Will")
            .surname("Smith")
            .marca("2222")
            .role("ADMIN")
            .build();

    private static TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private DepartmentRepo departmentRepo;
    private String baseUrl = "http://localhost:";
    @BeforeAll
    public static void init() {
        testRestTemplate = new TestRestTemplate();
    }
    @BeforeEach
    public void setup() {
        baseUrl = baseUrl.concat(String.valueOf(port)).concat("/api/users");
        usersRepo.deleteAll();
        usersRepo.save(firstUser);
        usersRepo.save(secondUser);
    }

    @Test
    public void testAddUser() {
        UserDto newUserDto = new UserDto().builder()
                .name("Bob")
                .surname("Bob")
                .departmentId(2)
                .marca("121")
                .role("ADMIN")
                .build();
        long size = usersRepo.count();
        UserDto response = testRestTemplate.withBasicAuth("Will", "2222")
                .postForObject(baseUrl + "/add", newUserDto, UserDto.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Bob", response.getName());
        Assertions.assertEquals(2, response.getDepartmentId());
        Assertions.assertEquals(size + 1, usersRepo.count());
    }

    @Test
    public void testUpdateUser() {
        UserDto newUserDto = new UserDto().builder()
                .name("Bob")
                .surname("Bob")
                .departmentId(2)
                .marca("0000")
                .role("MANAGER")
                .build();

        testRestTemplate.withBasicAuth("Will", "2222")
                .put(baseUrl + "/updateById/{id}", newUserDto, secondUser.getUserId());
        Optional<User> updated_user = usersRepo.findById(secondUser.getUserId());
        updated_user.ifPresent(user -> assertEquals("Bob", user.getName()));
    }

    @Test
    public void getAllUsers() throws IOException {
        String result = testRestTemplate.withBasicAuth("Will", "2222")
                .getForObject(baseUrl, String.class);
        List<UserDto> allUsers = new ObjectMapper().readValue(result, new TypeReference<List<UserDto>>() {
        });
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        assertEquals("Bill", allUsers.get(0).getName());
        assertEquals("2222", allUsers.get(1).getMarca());
    }

    @Test
    public void testGetById() throws IOException {
        String result = testRestTemplate.withBasicAuth("Will", "2222")
                .getForObject(baseUrl + "/getById/{id}", String.class, firstUser.getUserId());
        List<UserDto> resultList = new ObjectMapper().readValue(
                result, new TypeReference<List<UserDto>>() {
                });
        Optional<Department> found_department = departmentRepo.findById(resultList.get(0).getDepartmentId());
        assertNotNull(found_department);
        found_department.ifPresent(department -> assertEquals("Administration",department.getDepartmentName()));
        assertEquals("1111", resultList.get(0).getMarca());
        assertEquals("USER", resultList.get(0).getRole());
    }

    @Test
    public void testGetById_NotFound() throws IOException {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("Will", "2222")
                .getForEntity(baseUrl + "/getById/{id}", String.class, 32748);
        List<UserDto> resultList = new ObjectMapper().readValue(
                response.getBody(), new TypeReference<List<UserDto>>() {});
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testDeleteAll(){
        testRestTemplate.withBasicAuth("Will", "2222")
                .delete(baseUrl + "/delete");
        assertEquals(0, usersRepo.findAll().size());
    }

    @Test
    public void testDeleteAll_WrongCredentials(){
        testRestTemplate.withBasicAuth("Bill", "2222")
                .delete(baseUrl + "/delete");
        assertEquals(2, usersRepo.findAll().size());
    }

    @Test
    public void testDeleteById() {
        testRestTemplate.withBasicAuth("Will", "2222")
                .delete(baseUrl + "/deleteById/{id}", secondUser.getUserId());
        assertEquals(1, usersRepo.findAll().size());
        assertEquals("Bill", usersRepo.findAll().get(0).getName());
    }

    @Test
    public void testDeleteById_NotFound() {
        testRestTemplate.withBasicAuth("Will", "2222")
                .delete(baseUrl + "/deleteById/{id}", 10000);
        assertEquals(2, usersRepo.findAll().size());
    }
}