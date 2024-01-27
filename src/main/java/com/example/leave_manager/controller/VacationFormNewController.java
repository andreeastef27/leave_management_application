package com.example.leave_manager.controller;

import com.example.leave_manager.dto.VacationFormDto;
import com.example.leave_manager.service.VacationFormService;
import lombok.AllArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/api/vacationForm")
public class VacationFormNewController {
    private VacationFormService vacationFormService;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }

    @GetMapping("/getAll")
    public String getAllForms(Model model) {
        model.addAttribute("vacationForms", vacationFormService.getAllVacationForms());
        return "vacationForms";
    }

    @GetMapping("/newVacationForm")
    public String addVacationForm(Model model) {
        VacationFormDto vacationForm = new VacationFormDto();
        model.addAttribute("vacationForm", vacationForm);
        return "createVacationForm";
    }

    @PostMapping("/saveVacationForm")
    public String saveVacationForm(@ModelAttribute("vacationForm") VacationFormDto vacationForm) {
        vacationFormService.createVacationForm(vacationForm);
        return "redirect:/api/vacationForm/getAll";
    }
}
