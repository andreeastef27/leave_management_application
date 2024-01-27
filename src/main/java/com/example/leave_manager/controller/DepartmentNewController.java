package com.example.leave_manager.controller;

import com.example.leave_manager.dto.DepartmentDto;
import com.example.leave_manager.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api/department")
public class DepartmentNewController {
    private DepartmentService departmentService;

    @GetMapping("/getAllDepartments")
    public String getAll(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments";
    }

    @GetMapping("/newDepartment")
    public String createDepartment(Model model) {
        DepartmentDto department = new DepartmentDto();
        model.addAttribute("department", department);
        return "create_department";
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute("department") DepartmentDto department) {
        departmentService.addDepartment(department);
        return "redirect:/api/department/getAllDepartments";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable int id, Model model) {
        DepartmentDto oldDepartment = departmentService.getDepartmentById(id).get(0);
        model.addAttribute("oldDepartment", oldDepartment);
        return "edit_department";
    }

    @PostMapping("/updateDepartment/{id}")
    public String updateDepartment(@PathVariable int id,
                                   @ModelAttribute("department") DepartmentDto newDepartment,
                                   Model model) {
        departmentService.updateDepartment(id, newDepartment);
        return "redirect:/api/department/getAllDepartments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        departmentService.deleteDepartmentById(id);
        return "redirect:/api/department/getAllDepartments";
    }
}
