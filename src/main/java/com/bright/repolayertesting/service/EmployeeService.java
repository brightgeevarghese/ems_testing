package com.bright.repolayertesting.service;

import com.bright.repolayertesting.dto.request.EmployeeRequestDto;
import com.bright.repolayertesting.dto.response.EmployeeResponseDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeResponseDto> createEmployee(EmployeeRequestDto employeeRequestDto);
    List<EmployeeResponseDto> getAllEmployees();
    List<EmployeeResponseDto> findByFirstName(String firstName);
    List<EmployeeResponseDto> findByLastName(String lastName);
    List<EmployeeResponseDto> findByDepartmentCode(String departmentCode);
    Optional<EmployeeResponseDto> findByEmail(String email);
    Optional<EmployeeResponseDto> updateEmployee(String email, EmployeeRequestDto employeeRequestDto);
    Optional<EmployeeResponseDto> updateEmployeePartially(String email, EmployeeRequestDto employeeRequestDto);
    void deleteEmployee(String email);
}
