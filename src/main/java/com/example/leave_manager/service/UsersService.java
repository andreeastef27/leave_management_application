package com.example.leave_manager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.mapper.UserMapper;
import com.example.leave_manager.model.User;
import com.example.leave_manager.repository.UsersRepo;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {

    private UsersRepo usersRepo;

    public List<UserDto> getAllUsers() {
        return usersRepo.findAll()
                .stream()
                .map(UserMapper.mapper::convertEntityToDto)
                .toList();
    }

    public List<UserDto> getUserById(int id) {
        return usersRepo.findById(id)
                .stream()
                .map(UserMapper.mapper::convertEntityToDto)
                .toList();
    }

    public void createUser(UserDto userDto) {
        User user = UserMapper.mapper.convertDtoToEntity(userDto);
        usersRepo.save(user);
    }

    public void modifyUser(int id, UserDto newUserDto) {
        User existingUser = usersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Does not exist!"));

        User newUser = UserMapper.mapper.convertDtoToEntity(newUserDto);
        existingUser.setName(newUser.getName());
        existingUser.setMarca(newUser.getMarca());
        existingUser.setSurname(newUser.getSurname());
        existingUser.setRole(newUser.getRole());
        usersRepo.save(existingUser);
    }

    public String deleteUserById(int id) {
        Optional<User> user = usersRepo.findById(id);
        if (user.isPresent()) {
            usersRepo.deleteById(id);
            return "Deleted";
        } else {
            return "Not Found";
        }
    }

    public void deleteAllUsers() {
        usersRepo.deleteAll();
    }
}
