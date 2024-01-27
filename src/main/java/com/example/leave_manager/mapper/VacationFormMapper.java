package com.example.leave_manager.mapper;

import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.model.VacationForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VacationFormMapper {

    VacationFormMapper mapper = Mappers.getMapper(VacationFormMapper.class);

    VacationFormDto vacationFormToDto(VacationForm vacationForm);

    VacationForm vacationFormDtoToEntity(VacationFormDto vacationFormDto);
}
