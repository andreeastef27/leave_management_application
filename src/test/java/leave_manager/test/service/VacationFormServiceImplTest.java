package leave_manager.test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.mapper.VacationFormMapper;
import com.example.leave_manager.model.VacationForm;
import com.example.leave_manager.repository.VacationFormRepo;
import com.example.leave_manager.service.VacationFormServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationFormServiceImplTest {

    @Mock
    private VacationFormRepo vacationFormRepo;
    @InjectMocks
    private VacationFormServiceImpl vacationFormService;

    @Test
    void canCreateVacationForm() {
        //given
        when(vacationFormRepo.save(Mockito.any(VacationForm.class))).thenReturn(getVacationForm());
        //when
        VacationFormDto savedVacationForm = vacationFormService.createVacationForm(getVacationFormDto());
        //then
        assertThat(savedVacationForm).isNotNull();
        assertThat(savedVacationForm).isEqualTo(getVacationFormDto());
        verify(vacationFormRepo).save(Mockito.any(VacationForm.class));
    }

    @Test
    void canGetVacationFormById() {
        //given
        when(vacationFormRepo.findById(getVacationForm().getFormId())).thenReturn(Optional.of(getVacationForm()));
        //when
        List<VacationFormDto> savedVacationForm = vacationFormService.getVacationFormById(getVacationForm().getFormId());
        //then
        assertThat(savedVacationForm).isNotNull();
        assertThat(savedVacationForm).contains(getVacationFormDto());
        assertThat(savedVacationForm.get(0).getFormId()).isEqualTo(getVacationForm().getFormId());
        verify(vacationFormRepo).findById(getVacationForm().getFormId());
    }

    @Test
    void cannotGetVacationFormById() {
        //given
        when(vacationFormRepo.findById(getVacationForm().getFormId())).thenReturn(Optional.empty());
        //when
        List<VacationFormDto> savedVacationForm = vacationFormService.getVacationFormById(getVacationForm().getFormId());
        //then
        assertThat(savedVacationForm).isEmpty();
    }

    @Test
    void canGetAllVacationForms() {
        //given
        List<VacationForm> forms = List.of(getVacationForm(),
                VacationFormMapper.mapper.vacationFormDtoToEntity(getVacationFormDto()));
        when(vacationFormRepo.findAll()).thenReturn(forms);
        //when
        List<VacationFormDto> savedVacationsFormsDto = vacationFormService.getAllVacationForms();
        //then
        assertThat(savedVacationsFormsDto).isNotNull();
        assertThat(savedVacationsFormsDto.size()).isEqualTo(2);
        assertThat(savedVacationsFormsDto.get(0).getFormId()).isEqualTo(getVacationFormDto().getFormId());
        assertThat(savedVacationsFormsDto.get(1).getUserId()).isEqualTo(getVacationFormDto().getUserId());
        verify(vacationFormRepo).findAll();
    }

    @Test
    void canUpdateVacationForm() {
        //given
        when(vacationFormRepo.findById(getVacationForm().getFormId())).thenReturn(Optional.of(getVacationForm()));

        //when
        VacationFormDto savedVacationFormDto = vacationFormService.updateVacationForm(getVacationFormDto());

        //then
        ArgumentCaptor<VacationForm> vacationFormArgumentCaptor =
                ArgumentCaptor.forClass(VacationForm.class);
        verify(vacationFormRepo, times(1)).save(vacationFormArgumentCaptor.capture());
        VacationForm capturedVacationForm = vacationFormArgumentCaptor.getValue();
        assertThat(capturedVacationForm.getUserId()).isEqualTo(2);
        assertThat(savedVacationFormDto).isNull();
    }

    @Test
    void canThrowExceptionAtUpdateVacationForm() {
        when(vacationFormRepo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            vacationFormService.updateVacationForm(Mockito.mock(VacationFormDto.class));
        });
        verify(vacationFormRepo, never()).save(any(VacationForm.class));
    }

    @Test
    void deleteVacationForm() {
        assertAll(() -> vacationFormService.deleteVacationForm(anyInt()));
        verify(vacationFormRepo).deleteById(anyInt());
    }

    private VacationForm getVacationForm() {
        return VacationForm.builder()
                .userId(2)
                .vacationId(3)
                .startDate(new Date(2023, Calendar.OCTOBER, 10))
                .stopDate(new Date(2023, Calendar.NOVEMBER, 11))
                .build();
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