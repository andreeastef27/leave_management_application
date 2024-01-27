package com.example.leave_manager.controller;

import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/api/users")
@Controller
public class NewUserController {
    private UsersService usersService;

    @GetMapping("/getUsers")
    public String listUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String createUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "create_user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserDto user) {
        usersService.createUser(user);
        return "redirect:getUsers";
    }
}