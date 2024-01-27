package com.example.leave_manager.repository;

import com.example.leave_manager.model.VacationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationFormRepo extends JpaRepository<VacationForm, Integer> {

}
