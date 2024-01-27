package com.example.leave_manager.controller;

import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    UsersService usersService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public List<UserDto> getUserById(@PathVariable("id") int id) {
        return usersService.getUserById(id);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        usersService.createUser(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") int id, @RequestBody UserDto userDto) {
        usersService.modifyUser(id, userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id) {
        String response = usersService.deleteUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllUsers() {
        usersService.deleteAllUsers();
        return new ResponseEntity<>("Deleted!", HttpStatus.OK);
    }
}
