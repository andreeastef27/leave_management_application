package com.example.leave_manager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.mapper.VacationFormMapper;
import com.example.leave_manager.model.VacationForm;
import com.example.leave_manager.repository.VacationFormRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class VacationFormServiceImpl implements VacationFormService {

    private VacationFormRepo vacationFormRepo;

    @Override
    public VacationFormDto createVacationForm(VacationFormDto vacationFormDto) {
        VacationForm vacationForm = VacationFormMapper.mapper.vacationFormDtoToEntity(vacationFormDto);
        VacationForm savedVacationForm = vacationFormRepo.save(vacationForm);
        return VacationFormMapper.mapper.vacationFormToDto(savedVacationForm);
    }

    @Override
    public List<VacationFormDto> getVacationFormById(int vacationFormId) {
        return vacationFormRepo.findById(vacationFormId)
                .stream()
                .map(VacationFormMapper.mapper::vacationFormToDto)
                .toList();
    }

    @Override
    public List<VacationFormDto> getAllVacationForms() {
        List<VacationForm> vacationForms = vacationFormRepo.findAll();
        return vacationForms.stream().map(VacationFormMapper.mapper::vacationFormToDto)
                .toList();
    }

    @Override
    public VacationFormDto updateVacationForm(VacationFormDto vacationFormDto) {
        VacationForm existingVacationForm = vacationFormRepo.findById(vacationFormDto.getFormId())
                .orElseThrow(() -> new ResourceNotFoundException("Vacation form with this id does not exist"));
        existingVacationForm.setUserId(vacationFormDto.getUserId());
        existingVacationForm.setVacationId(vacationFormDto.getVacationId());
        existingVacationForm.setStartDate(vacationFormDto.getStartDate());
        existingVacationForm.setStopDate(vacationFormDto.getStopDate());
        VacationForm updatedVacationForm = vacationFormRepo.save(existingVacationForm);
        return VacationFormMapper.mapper.vacationFormToDto(updatedVacationForm);
    }

    @Override
    public void deleteVacationForm(int vacationFormId) {vacationFormRepo.deleteById(vacationFormId);}
}
