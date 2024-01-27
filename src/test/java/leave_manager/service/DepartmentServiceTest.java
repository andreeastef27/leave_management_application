package leave_manager.service;

import com.example.leave_manager.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.model.Department;
import com.example.leave_manager.repository.DepartmentRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepo departmentRepo;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void canAddDepartment() {
        departmentService.addDepartment(getDepartmentDto());
        ArgumentCaptor<Department> departmentArgumentCaptor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepo).save(departmentArgumentCaptor.capture());
        Department departmentCaptured = departmentArgumentCaptor.getValue();
        assertNotNull(departmentCaptured);
    }

    @Test
    public void testGetAllDepartments() {
        //given
        List<Department> departments = new ArrayList<>();
        departments.add(getDepartment());
        departments.add(getDepartment2());
        when(departmentRepo.findAll()).thenReturn(departments);
        //when
        List<DepartmentDto> savedDepartmentsDto = departmentService.getAllDepartments();
        //then
        assertThat(savedDepartmentsDto.size()).isEqualTo(2);
        assertThat(departments.get(0).getDepartmentName()).isEqualTo("HR");
    }

    @Test
    public void canGetDepartmentById() {
        when(departmentRepo.findById(getDepartment().getDepartmentId())).thenReturn(Optional.of(getDepartment()));
        List<DepartmentDto> savedDepartment = departmentService.getDepartmentById(getDepartment().getDepartmentId());
        assertThat(savedDepartment.get(0).getDepartmentName()).isEqualTo("HR");
    }

    @Test
    public void cannotGetDepartmentById() {
        when(departmentRepo.findById(getDepartment().getDepartmentId())).thenReturn(Optional.empty());
        List<DepartmentDto> savedDepartment = departmentService.getDepartmentById(getDepartment().getDepartmentId());
        assertThat(savedDepartment).isEmpty();
    }

    @Test
    void canGetDepartmentByName() {
        when(departmentRepo.findByDepartmentName(getDepartment().getDepartmentName())).thenReturn(Optional.of(getDepartment()));
        List<DepartmentDto> savedDepartment = departmentService.getDepartmentByName(getDepartment().getDepartmentName());
        assertThat(savedDepartment.get(0).getDepartmentName()).isEqualTo("HR");
    }

    @Test
    public void cannotGetDepartmentByName() {
        when(departmentRepo.findByDepartmentName(getDepartment().getDepartmentName())).thenReturn(Optional.empty());
        List<DepartmentDto> savedDepartment = departmentService.getDepartmentByName(getDepartment().getDepartmentName());
        assertThat(savedDepartment).isEmpty();
    }

    @Test
    public void canUpdateDepartment() {
        //given
        when(departmentRepo.findById(getDepartmentDto().getDepartmentId())).thenReturn(Optional.of(getDepartment()));
        //when
        departmentService.updateDepartment(getDepartmentDto().getDepartmentId(),getDepartmentDto());
        //then
        ArgumentCaptor<Department> departmentArgumentCaptor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepo, times(1)).save(departmentArgumentCaptor.capture());
        Department capturedDepartment = departmentArgumentCaptor.getValue();
        assertThat(capturedDepartment.getDepartmentId()).isEqualTo(4);
    }

    @Test
    void canThrowExceptionAtUpdateDepartment() {
        when(departmentRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.updateDepartment(getDepartmentDto().getDepartmentId(),getDepartmentDto());
        });
    }

    @Test
    void deleteDepartmentById() {
        assertAll(() -> departmentService.deleteDepartmentById(getDepartment().getDepartmentId()));
    }

    @Test
    void deleteDepartmentByName() {
        assertAll(() -> departmentService.deleteDepartmentByName(getDepartment().getDepartmentName()));
    }

    @Test
    public void testDeleteAllDepartments() {
        departmentService.deleteAllDepartments();
        verify(departmentRepo).deleteAll();
    }

    private Department getDepartment(){
        return Department.builder()
                .departmentId(4)
                .departmentName("HR")
                .build();
    }

    private Department getDepartment2(){
        return Department.builder()
                .departmentId(5)
                .departmentName("IT")
                .build();
    }

   private DepartmentDto getDepartmentDto(){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("HR");
        departmentDto.setDepartmentId(4);
        return departmentDto;
   }
}
