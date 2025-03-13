package com.bright.repolayertesting.repository;

import com.bright.repolayertesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFirstName(String firstName);
    List<Employee> findByLastName(String lastName);
    List<Employee> findByDepartmentCode(String departmentCode);
    Optional<Employee> findByEmail(String email);
}
