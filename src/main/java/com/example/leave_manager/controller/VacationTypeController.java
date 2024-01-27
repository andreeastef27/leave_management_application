package com.example.leave_manager.controller;

import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.service.VacationTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vacationType")
public class VacationTypeController {

    private VacationTypeService vacationTypeService;

    @PostMapping(path = "/create")
    public ResponseEntity<VacationTypeDto> createVacationType(@RequestBody VacationTypeDto vacationTypeDto) {
        VacationTypeDto savedVacationType = vacationTypeService.createVacationType(vacationTypeDto);
        return new ResponseEntity<>(savedVacationType, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<VacationTypeDto>> getAllVacationType() {
        List<VacationTypeDto> vacationsType = vacationTypeService.getAllVacationType();
        return new ResponseEntity<>(vacationsType, HttpStatus.OK);
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<List<VacationTypeDto>> getVacationsTypeById(@PathVariable("id") int vacationTypeId) {
        List<VacationTypeDto> vacationsTypeDto = vacationTypeService.getVacationsTypeById(vacationTypeId);
        return new ResponseEntity<>(vacationsTypeDto, HttpStatus.OK);
    }

    @GetMapping(path = "/getByType/{type}")
    public ResponseEntity<List<VacationTypeDto>> getVacationsTypeByType(@PathVariable("type") String type) {
        List<VacationTypeDto> vacationsTypeDto = vacationTypeService.getVacationsTypeByType(type);
        return new ResponseEntity<>(vacationsTypeDto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public void updateVacationType(@PathVariable("id") int vacationTypeId,
                                   @RequestBody VacationTypeDto vacationTypeDto) {
        vacationTypeDto.setVacationId(vacationTypeId);
        vacationTypeService.updateVacationType(vacationTypeId, vacationTypeDto);
    }

    @Transactional
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteVacationTypeById(@PathVariable("id") int vacationTypeId) {
        vacationTypeService.deleteVacationTypeById(vacationTypeId);
        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/deleteByType/{type}")
    public ResponseEntity<String> deleteVacationTypeByType(@PathVariable("type") String type) {
        vacationTypeService.deleteVacationTypeByType(type);
        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }
}
