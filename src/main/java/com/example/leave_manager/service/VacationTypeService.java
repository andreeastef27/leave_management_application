package com.example.leave_manager.service;

import org.springframework.stereotype.Service;
import com.example.leave_manager.dto.VacationTypeDto;

import java.util.List;

@Service
public interface VacationTypeService {

    VacationTypeDto createVacationType(VacationTypeDto vacationTypeDto);

    List<VacationTypeDto> getAllVacationType();

    List<VacationTypeDto> getVacationsTypeById(int vacationTypeId);

    List<VacationTypeDto> getVacationsTypeByType(String type);

    void updateVacationType(int id, VacationTypeDto newVacationTypeDto);

    void deleteVacationTypeById(int vacationTypeId);

    void deleteVacationTypeByType(String type);
}
