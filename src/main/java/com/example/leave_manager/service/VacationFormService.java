package com.example.leave_manager.service;

import com.example.leave_manager.dto.VacationFormDto;

import java.util.List;

public interface VacationFormService {

    VacationFormDto createVacationForm(VacationFormDto vacationForm);

    List<VacationFormDto> getVacationFormById(int vacationFormId);

    List<VacationFormDto> getAllVacationForms();

    VacationFormDto updateVacationForm(VacationFormDto vacationForm);

    void deleteVacationForm(int vacationFormId);
}
