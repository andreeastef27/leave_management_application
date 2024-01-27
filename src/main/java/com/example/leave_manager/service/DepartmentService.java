package com.example.leave_manager.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.mapper.DepartmentMapper;
import com.example.leave_manager.model.Department;
import com.example.leave_manager.repository.DepartmentRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentService {

    private DepartmentRepo departmentRepo;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepo.findAll()
                .stream()
                .map(DepartmentMapper.mapper::convertEntityToDto)
                .toList();
    }

    public List<DepartmentDto> getDepartmentById(int id) {
        return departmentRepo.findById(id)
                .stream()
                .map(DepartmentMapper.mapper::convertEntityToDto)
                .toList();
    }

    public List<DepartmentDto> getDepartmentByName(String name) {
        return departmentRepo.findByDepartmentName(name)
                .stream()
                .map(DepartmentMapper.mapper::convertEntityToDto)
                .toList();
    }

    public void addDepartment(DepartmentDto departmentDto) {
        Department department = DepartmentMapper.mapper.convertDTOtoEntity(departmentDto);
        departmentRepo.save(department);
    }

    public void deleteAllDepartments() {
        departmentRepo.deleteAll();
    }

    public void deleteDepartmentById(int id) {
        departmentRepo.deleteById(id);
    }

    @Transactional
    public void deleteDepartmentByName(String name) {
        departmentRepo.deleteByDepartmentName(name);
    }

    public void updateDepartment(int id, DepartmentDto newDepartmentDto) {
        Department oldDepartment = departmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department " + id + " does not exist"));

        Department newDepartment = DepartmentMapper.mapper.convertDTOtoEntity(newDepartmentDto);
        oldDepartment.setDepartmentName(newDepartment.getDepartmentName());

        departmentRepo.save(oldDepartment);
    }

    public void updateDepartmentFoundByName(String name, DepartmentDto newDepartmentDto) {
        Department oldDepartment = departmentRepo.findByDepartmentName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department " + name + " does not exist"));

        Department newDepartment = DepartmentMapper.mapper.convertDTOtoEntity(newDepartmentDto);
        oldDepartment.setDepartmentName(newDepartment.getDepartmentName());

        departmentRepo.save(oldDepartment);
    }
}
