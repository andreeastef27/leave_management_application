package leave_manager.service;

import com.example.leave_manager.service.VacationTypeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.mapper.VacationTypeMapper;
import com.example.leave_manager.model.VacationType;
import com.example.leave_manager.repository.VacationTypeRepo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationTypeServiceImplTest {

    @Mock
    private VacationTypeRepo vacationTypeRepo;
    @InjectMocks
    private VacationTypeServiceImpl vacationTypeService;

    @BeforeEach
    void setUp() {
        vacationTypeService = new VacationTypeServiceImpl(vacationTypeRepo);
    }

    @Test
    void canGetAllVacationType() {
        List<VacationType> vacationsType = List.of(getVacationType(),
                VacationTypeMapper.MAPPER.vacationTypeDtoToEntity(getVacationTypeDto()));
        when(vacationTypeRepo.findAll()).thenReturn(vacationsType);
        List<VacationTypeDto> savedVacationsTypeDto = vacationTypeService.getAllVacationType();
        Assertions.assertThat(savedVacationsTypeDto).isNotNull();
        Assertions.assertThat(savedVacationsTypeDto.size()).isEqualTo(2);
        Assertions.assertThat(savedVacationsTypeDto.get(0).getVacationId()).isEqualTo(getVacationTypeDto().getVacationId());
        Assertions.assertThat(savedVacationsTypeDto.get(1).getType()).isEqualTo(getVacationTypeDto().getType());
        verify(vacationTypeRepo).findAll();
    }

    @Test
    void canGetVacationsTypeById() {
        when(vacationTypeRepo.findById(getVacationType().getVacationId())).thenReturn(Optional.of(getVacationType()));
        List<VacationTypeDto> savedVacationsType = vacationTypeService.getVacationsTypeById(getVacationType().getVacationId());
        Assertions.assertThat(savedVacationsType.get(0).getType()).isEqualTo("Test");
    }

    @Test
    void canGetVacationsTypeByType() {
        when(vacationTypeRepo.findByType(getVacationType().getType())).thenReturn(Optional.of(getVacationType()));
        List<VacationTypeDto> savedVacationsType = vacationTypeService.getVacationsTypeByType(getVacationType().getType());
        Assertions.assertThat(savedVacationsType.get(0).getVacationId()).isEqualTo(100);
    }

    @Test
    void canCreateVacationType() {
        when(vacationTypeRepo.save(Mockito.any(VacationType.class))).thenReturn(getVacationType());
        VacationTypeDto savedVacationType = vacationTypeService.createVacationType(getVacationTypeDto());
        Assertions.assertThat(savedVacationType).isNotNull();
        Assertions.assertThat(savedVacationType).isEqualTo(getVacationTypeDto());
        Assertions.assertThat(savedVacationType.getType()).isEqualTo(getVacationTypeDto().getType());
        verify(vacationTypeRepo).save(Mockito.any(VacationType.class));

    }

    @Test
    void updateVacationType() {
        when(vacationTypeRepo.findById(getVacationTypeDto().getVacationId())).thenReturn(Optional.of(getVacationType()));
        vacationTypeService.updateVacationType(getVacationTypeDto().getVacationId(), getVacationTypeDto());
        ArgumentCaptor<VacationType> vacationTypeArgumentCaptor =
                ArgumentCaptor.forClass(VacationType.class);
        verify(vacationTypeRepo, times(1)).save(vacationTypeArgumentCaptor.capture());
        VacationType capturedVacationType = vacationTypeArgumentCaptor.getValue();
        assertThat(capturedVacationType.getVacationId()).isEqualTo(100);
    }

    @Test
    void canThrowExceptionAtUpdateVacationType() {
        when(vacationTypeRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            vacationTypeService.updateVacationType(getVacationTypeDto().getVacationId(), getVacationTypeDto());
        });
    }

    @Test
    void deleteVacationTypeById() {
        vacationTypeService.deleteVacationTypeById(getVacationType().getVacationId());
        verify(vacationTypeRepo).deleteById(getVacationType().getVacationId());
    }

    @Test
    void deleteVacationTypeByType() {
        vacationTypeService.deleteVacationTypeByType(getVacationType().getType());
        verify(vacationTypeRepo).deleteByType(getVacationType().getType());
    }

    private VacationType getVacationType() {
        return VacationType.builder()
                .vacationId(100)
                .type("Test")
                .build();
    }

    private VacationTypeDto getVacationTypeDto() {
        return VacationTypeDto.builder()
                .vacationId(100)
                .type("Test")
                .build();
    }
}