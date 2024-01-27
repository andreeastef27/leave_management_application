package com.example.leave_manager.controller;

import com.example.leave_manager.dto.VacationTypeDto;
import com.example.leave_manager.model.VacationType;
import com.example.leave_manager.service.VacationTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/api/vacationType")
@Controller
public class VacationTypePageController {
    private VacationTypeService vacationTypeService;

    @GetMapping("/getAllVacations")
    public String listAllVacations(Model model) {
        model.addAttribute("vacations", vacationTypeService.getAllVacationType());
        return "vacations";
    }

    @GetMapping("/getSaved")
    public String createNewVacationType(Model model) {
        VacationType vacationType = new VacationType();
        model.addAttribute("vacation", vacationType);
        return "saved-vacationType";
    }

    @PostMapping("/saveVacationType")
    public String saveVacationType(@ModelAttribute("vacation") VacationTypeDto vacationTypeDto) {
        vacationTypeService.createVacationType(vacationTypeDto);
        return "redirect:/api/vacationType/getAllVacations";
    }
}
