package com.example.leave_manager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.mapper.VacationTypeMapper;
import com.example.leave_manager.model.VacationType;
import com.example.leave_manager.repository.VacationTypeRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class VacationTypeServiceImpl implements VacationTypeService {

    private VacationTypeRepo vacationTypeRepo;

    @Override
    public VacationTypeDto createVacationType(VacationTypeDto vacationTypeDto) {
        VacationType vacationType = VacationTypeMapper.MAPPER.vacationTypeDtoToEntity(vacationTypeDto);
        VacationType savedVacationType = vacationTypeRepo.save(vacationType);
        return VacationTypeMapper.MAPPER.vacationTypeToDto(savedVacationType);
    }

    @Override
    public List<VacationTypeDto> getAllVacationType() {
        List<VacationType> vacationsType = vacationTypeRepo.findAll();
        return vacationsType.stream()
                .map(VacationTypeMapper.MAPPER::vacationTypeToDto)
                .toList();
    }

    @Override
    public List<VacationTypeDto> getVacationsTypeById(int vacationTypeId) {
        return vacationTypeRepo.findById(vacationTypeId).stream()
                .map(VacationTypeMapper.MAPPER::vacationTypeToDto)
                .toList();

    }

    @Override
    public List<VacationTypeDto> getVacationsTypeByType(String type) {
        return vacationTypeRepo.findByType(type).stream()
                .map(VacationTypeMapper.MAPPER::vacationTypeToDto)
                .toList();
    }

    @Override
    public void updateVacationType(int id, VacationTypeDto newVacationTypeDto) {
        VacationType oldVacationType = vacationTypeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VacationType id " + id + " does not exist"));
        oldVacationType.setType(newVacationTypeDto.getType());
        vacationTypeRepo.save(oldVacationType);
    }

    @Override
    public void deleteVacationTypeById(int vacationTypeId) {

        vacationTypeRepo.deleteById(vacationTypeId);
    }

    @Override
    public void deleteVacationTypeByType(String type) {

        vacationTypeRepo.deleteByType(type);
    }
}
