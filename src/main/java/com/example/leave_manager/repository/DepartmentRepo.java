package com.example.leave_manager.repository;

import com.example.leave_manager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {

    Optional<Department> findByDepartmentName(String name);
    void deleteByDepartmentName(String name);
}
