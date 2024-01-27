package com.example.leave_manager.controller;

import com.example.leave_manager.model.Employee;
import com.example.leave_manager.repository.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/employee")
public class EmployeeController {

    private EmployeeRepo employeeRepo;

    @DeleteMapping("{address}")
    @Transactional
    public ResponseEntity<String> deleteEmployee(@PathVariable("address") String address) {
        employeeRepo.deleteByAddress(address);
        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> insertEmployee(@RequestBody Employee employee) {
        employeeRepo.save(employee);
        return new ResponseEntity<>("User added!", HttpStatus.CREATED);
    }
}
