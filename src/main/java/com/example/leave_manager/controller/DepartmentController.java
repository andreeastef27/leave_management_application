package com.example.leave_manager.controller;

import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {

    private DepartmentService departmentService;

    @GetMapping("/getAll")
    public List<DepartmentDto> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/getById/{id}")
    public List<DepartmentDto> getDepartmentById(@PathVariable("id") int id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("/getByName/{name}")
    public List<DepartmentDto> getDepartmentbyName(@PathVariable("name") String name) {
        return departmentService.getDepartmentByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentDto departmentDto) {
        departmentService.addDepartment(departmentDto);
        return new ResponseEntity<>("Department added!", HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllDepartments() {
        departmentService.deleteAllDepartments();
        return new ResponseEntity<>("All departments deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteDepartmentById(@PathVariable("id") int id) {
        departmentService.deleteDepartmentById(id);
        return new ResponseEntity<>("Department with id " + id + " deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteByName/{name}")
    public ResponseEntity<String> deleteDepartmentByName(@PathVariable String name) {
        departmentService.deleteDepartmentByName(name);
        return new ResponseEntity<>("Department " + name + " deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteByName")
    public ResponseEntity<String> deleteByNameParam(@RequestParam(value = "name") String name) {
        departmentService.deleteDepartmentByName(name);
        return new ResponseEntity<>("Department " + name + " deleted!", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable("id") int id, @RequestBody DepartmentDto departmentDto) {
        departmentService.updateDepartment(id, departmentDto);
        return new ResponseEntity<>("Department with id " + id + " updated!", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDepartmentFoundByName(@RequestParam(value = "name") String name, @RequestBody DepartmentDto departmentDto) {
        departmentService.updateDepartmentFoundByName(name, departmentDto);
        return new ResponseEntity<>("Department " + name + " updated to " + departmentDto.getDepartmentName() + "!", HttpStatus.OK);
    }
}
