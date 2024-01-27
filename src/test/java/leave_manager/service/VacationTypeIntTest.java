package leave_manager.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.mapper.VacationTypeMapper;
import com.example.leave_manager.model.VacationType;
import com.example.leave_manager.repository.VacationTypeRepo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VacationTypeIntTest {
    private static TestRestTemplate restTemplate;
    @Autowired
    private VacationTypeRepo vacationTypeRepo;
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/vacationType");
    }

    @Test
    @Order(1)
    public void testCreateVacationType() {
        VacationTypeDto vacationTypeDto = restTemplate.withBasicAuth("Will", "2222")
                .postForObject(baseUrl.concat("/create"), getVacationTypeDto(), VacationTypeDto.class);
        Optional<VacationType> vacationType = vacationTypeRepo.findByType(getVacationTypeDto().getType());
        assertNotNull(vacationTypeDto);
        assertNotNull(vacationType);
        assertEquals(getVacationTypeDto().getType(), vacationTypeDto.getType());
    }

    @Test
    @Order(2)
    public void testGetAllVacationsType() {
        ResponseEntity<List<VacationTypeDto>> vacationsTypeDto = restTemplate.withBasicAuth("Will", "2222")
                .exchange(baseUrl + "/getAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<VacationTypeDto>>() {
                });
        List<VacationTypeDto> responseGetAll = vacationsTypeDto.getBody();
        assertNotNull(responseGetAll);
        assertEquals(1, responseGetAll.get(0).getVacationId());
    }

    @Test
    @Order(3)
    public void testGetById() {
        Optional<VacationType> optionalVacationType = vacationTypeRepo.findByType(getVacationTypeDto().getType());
        optionalVacationType.ifPresent(optional -> {
            VacationTypeDto responseCreate = VacationTypeMapper.MAPPER.vacationTypeToDto(optional);
            String responseGetBy = restTemplate.withBasicAuth("Will", "2222")
                    .getForObject(baseUrl + "/getById/{id}", String.class, responseCreate.getVacationId());
            try {
                List<VacationTypeDto> resultList = new ObjectMapper().readValue(responseGetBy, new TypeReference<List<VacationTypeDto>>() {
                });
                assertEquals(responseCreate.getVacationId(), resultList.get(0).getVacationId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @Order(4)
    public void testGetByType() {
        Optional<VacationType> optionalVacationType = vacationTypeRepo.findByType(getVacationTypeDto().getType());
        optionalVacationType.ifPresent(op -> {
            VacationTypeDto responseCreate = VacationTypeMapper.MAPPER.vacationTypeToDto(op);
            String responseGetBy = restTemplate.withBasicAuth("Will", "2222")
                    .getForObject(baseUrl + "/getByType/{type}", String.class, responseCreate.getType());
            try {
                List<VacationTypeDto> resultList = new ObjectMapper().readValue(responseGetBy, new TypeReference<List<VacationTypeDto>>() {
                });
                assertEquals(responseCreate.getVacationId(), resultList.get(0).getVacationId());
                assertEquals(responseCreate.getType(), resultList.get(0).getType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @Order(5)
    public void testUpdateVacationType() {
        Optional<VacationType> optionalVacationType = vacationTypeRepo.findByType(getVacationTypeDto().getType());
        optionalVacationType.ifPresent(op -> {
            VacationTypeDto responseCreate = VacationTypeMapper.MAPPER.vacationTypeToDto(op);
            restTemplate.withBasicAuth("Will", "2222")
                    .put(baseUrl + "/update/{id}", getNewVacationTypeDto(), responseCreate.getVacationId());
            Optional<VacationType> savedDto = vacationTypeRepo.findById(responseCreate.getVacationId());
            assertNotNull(savedDto);
            savedDto.ifPresent(vacationType -> {
                assertEquals(vacationType.getType(), getNewVacationTypeDto().getType());
            });
        });
    }

    @Test
    @Order(6)
    public void testDeleteVacationType() {
        Optional<VacationType> optionalVacationType = vacationTypeRepo.findByType(getNewVacationTypeDto().getType());
        optionalVacationType.ifPresent(op -> {
            VacationTypeDto responseCreate = VacationTypeMapper.MAPPER.vacationTypeToDto(op);
            ResponseEntity<String> resp = restTemplate.withBasicAuth("Will", "2222")
                    .exchange(baseUrl + "/deleteById/{id}", HttpMethod.DELETE, null, String.class, responseCreate.getVacationId());
            Optional<VacationType> vacationTypeOptional = vacationTypeRepo.findByType(getNewVacationTypeDto().getType());
            assertTrue(vacationTypeOptional.isEmpty());
        });
    }

    private VacationTypeDto getNewVacationTypeDto() {
        return VacationTypeDto.builder()
                .type("Updated Integration Test!")
                .build();
    }

    private VacationTypeDto getVacationTypeDto() {
        return VacationTypeDto.builder()
                .type("New Integration Test!")
                .build();
    }
}
