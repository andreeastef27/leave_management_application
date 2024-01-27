package com.example.leave_manager.controller;

import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.service.VacationFormService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/vacationForm")
public class VacationFormController {

    private VacationFormService vacationFormService;

    @PostMapping(path = "/createForm")
    public ResponseEntity<VacationFormDto> createVacationForm(@RequestBody VacationFormDto form) {
        VacationFormDto savedVacationForm = vacationFormService.createVacationForm(form);
        return new ResponseEntity<>(savedVacationForm, HttpStatus.CREATED);
    }

    @GetMapping(path = "/getFormById/{id}")
    public List<VacationFormDto> getVacationFormById(@PathVariable("id") int vacationFormId) {
        return vacationFormService.getVacationFormById(vacationFormId);
    }

    @GetMapping(path = "/getAllForms")
    public ResponseEntity<List<VacationFormDto>> getAllVacationForms() {
        var vacationForms = vacationFormService.getAllVacationForms();
        return new ResponseEntity<>(vacationForms, HttpStatus.OK);
    }

    @PutMapping(path = "/putById/{id}")
    public ResponseEntity<VacationFormDto> updateVacationForm(@PathVariable("id") int vacationFormId,
                                                              @RequestBody VacationFormDto vacationForm) {
        vacationForm.setFormId(vacationFormId);
        VacationFormDto updatedForm = vacationFormService.updateVacationForm(vacationForm);
        return new ResponseEntity<>(updatedForm, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<String> deleteVacationForm(@PathVariable("id") int vacationFormId) {
        vacationFormService.deleteVacationForm(vacationFormId);
        return new ResponseEntity<>("Vacation form successfully deleted!", HttpStatus.OK);
    }
}
