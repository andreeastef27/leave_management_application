package com.example.leave_manager.mapper;

import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentMapper mapper = Mappers.getMapper(DepartmentMapper.class);

    public DepartmentDto convertEntityToDto(Department department);

    public Department convertDTOtoEntity(DepartmentDto departmentDto);
}
