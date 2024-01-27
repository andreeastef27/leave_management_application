package leave_manager.test.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.example.leave_manager.LeaveManagerApplication;
import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.mapper.VacationFormMapper;
import com.example.leave_manager.model.VacationForm;
import com.example.leave_manager.model.VacationType;
import com.example.leave_manager.repository.VacationFormRepo;
import com.example.leave_manager.repository.VacationTypeRepo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeaveManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VacationFormIntegrationTests {
    private static TestRestTemplate testRestTemplate;
    private final VacationForm defaultVacationForm = VacationForm.builder()
            .userId(3)
            .vacationId(7)
            .startDate(new Date(2018, Calendar.JANUARY, 1))
            .stopDate(new Date(2018, Calendar.FEBRUARY, 2))
            .build();
    private String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private VacationFormRepo vacationFormRepo;
    @Autowired
    private VacationTypeRepo vacationTypeRepo;

    @BeforeAll
    public static void init() {
        testRestTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setup() {
        baseUrl = baseUrl.concat(String.valueOf(port)).concat("/api/vacationForm");

        vacationFormRepo.deleteAll();
        vacationFormRepo.save(defaultVacationForm);
    }

    @Test
    public void testCreateVacationForm() {
        VacationFormDto response = testRestTemplate.withBasicAuth("Cliff", "3333")
                .postForObject(baseUrl + "/createForm", getVacationFormDto(), VacationFormDto.class);
        Optional<VacationType> vacationType = vacationTypeRepo.findById(response.getVacationId());

        assertNotNull(response);
        assertNotNull(vacationType);
        assertEquals(2, response.getUserId());
        vacationType.ifPresent(type -> assertEquals("Absenta neplatita", type.getType()));
        assertEquals(2, vacationFormRepo.findAll().size());
    }

    @Test
    public void testGetAllForms() throws IOException {
        VacationForm oneMore = VacationFormMapper.mapper
                .vacationFormDtoToEntity(getVacationFormDto());
        vacationFormRepo.save(oneMore);
        String result = testRestTemplate.withBasicAuth("Bill", "1111")
                .getForObject(baseUrl + "/getAllForms", String.class);
        List<VacationFormDto> resultList = new ObjectMapper().readValue(
                result, new TypeReference<List<VacationFormDto>>() {
                });

        assertEquals(2, resultList.size());
        assertEquals(7, resultList.get(0).getVacationId());
        assertEquals(3, resultList.get(1).getVacationId());

        vacationFormRepo.deleteById(oneMore.getFormId());
    }

    @Test
    public void testGetFormById() throws IOException {
        String result = testRestTemplate.withBasicAuth("Bill", "1111")
                .getForObject(baseUrl + "/getFormById/{id}", String.class, defaultVacationForm.getFormId());
        List<VacationFormDto> resultList = new ObjectMapper().readValue(
                result, new TypeReference<List<VacationFormDto>>() {
                });
        Optional<VacationType> vacationType = vacationTypeRepo.findById(resultList.get(0).getVacationId());

        assertEquals(3, resultList.get(0).getUserId());
        assertEquals(7, resultList.get(0).getVacationId());
        vacationType.ifPresent(type -> assertEquals("Absenta suspendare", type.getType()));
    }

    @Test
    public void testPutById() {
        testRestTemplate.withBasicAuth("Cliff", "3333")
                .put(baseUrl + "/putById/{id}", getVacationFormDto(), defaultVacationForm.getFormId());
        Optional<VacationForm> form = vacationFormRepo.findById(defaultVacationForm.getFormId());

        form.ifPresent(vacationForm -> assertEquals(2, vacationForm.getUserId()));
        form.ifPresent(vacationForm -> assertEquals(3, vacationForm.getVacationId()));
    }

    @Test
    public void testDeleteById() {
        testRestTemplate.withBasicAuth("Will", "2222")
                .delete(baseUrl + "/deleteById/{id}", defaultVacationForm.getFormId());

        assertEquals(0, vacationFormRepo.findAll().size());
    }

    private VacationFormDto getVacationFormDto() {
        return VacationFormDto.builder()
                .userId(2)
                .vacationId(3)
                .startDate(new Date(2023, Calendar.OCTOBER, 10))
                .stopDate(new Date(2023, Calendar.NOVEMBER, 11))
                .build();
    }
}