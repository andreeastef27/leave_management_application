package com.example.leave_manager.repository;

import com.example.leave_manager.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepo extends CrudRepository<Employee, String> {

    //List<Employee> findAll();
    Employee findByName(String name);

    Employee save(Employee e);

    void deleteByAddress(String address);
}
