package com.example.leave_manager.repository;

import com.example.leave_manager.model.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationTypeRepo extends JpaRepository<VacationType, Integer> {

    void deleteByType(String type);
    Optional<VacationType> findByType(String type);
}
