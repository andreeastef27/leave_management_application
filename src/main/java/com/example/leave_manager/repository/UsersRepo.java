package com.example.leave_manager.repository;

import com.example.leave_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {

    void deleteByName(String name);
}
