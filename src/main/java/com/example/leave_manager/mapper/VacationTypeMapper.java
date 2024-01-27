package com.example.leave_manager.mapper;

import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.model.VacationType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VacationTypeMapper {
    VacationTypeMapper MAPPER = Mappers.getMapper(VacationTypeMapper.class);

    VacationType vacationTypeDtoToEntity(VacationTypeDto vacationTypeDto);

    VacationTypeDto vacationTypeToDto(VacationType vacationType);
}
