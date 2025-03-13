package com.bright.repolayertesting.service.impl;

import com.bright.repolayertesting.dto.request.EmployeeRequestDto;
import com.bright.repolayertesting.dto.response.EmployeeResponseDto;
import com.bright.repolayertesting.model.Employee;
import com.bright.repolayertesting.repository.EmployeeRepository;
import com.bright.repolayertesting.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Creates a new Employee based on employeeRequestDto
     *
     * @param employeeRequestDto The data transfer object which contains the information of new Employee
     * @return An 'Optional' containing an 'EmployeeResponseDto'
     * @throws IllegalArgumentException if `employeeRequestDto` is null or contains invalid data.
     * @throws 
     */
    @Override
    public Optional<EmployeeResponseDto> createEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee newEmployee = new Employee(employeeRequestDto.firstName(), employeeRequestDto.lastName(), employeeRequestDto.email(), employeeRequestDto.departmentCode());
        Employee savedEmployee = employeeRepository.save(newEmployee);
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(savedEmployee.getFirstName(), savedEmployee.getLastName(), savedEmployee.getEmail());
        return Optional.of(employeeResponseDto);
    }

    /**
     * @return
     */
    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponseDtos.add(new EmployeeResponseDto(employee.getFirstName(), employee.getLastName(), employee.getEmail()));
        }
        return employeeResponseDtos;
    }

    /**
     * @param firstName
     * @return
     */
    @Override
    public List<EmployeeResponseDto> findByFirstName(String firstName) {
        List<Employee> employees = employeeRepository.findByFirstName(firstName);
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponseDtos.add(new EmployeeResponseDto(employee.getFirstName(), employee.getLastName(), employee.getEmail()));
        }
        return employeeResponseDtos;
    }

    /**
     * @param lastName
     * @return
     */
    @Override
    public List<EmployeeResponseDto> findByLastName(String lastName) {
        List<Employee> employees = employeeRepository.findByLastName(lastName);
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponseDtos.add(new EmployeeResponseDto(employee.getFirstName(), employee.getLastName(), employee.getEmail()));
        }
        return employeeResponseDtos;
    }

    /**
     * @param departmentCode
     * @return
     */
    @Override
    public List<EmployeeResponseDto> findByDepartmentCode(String departmentCode) {
        List<Employee> employees = employeeRepository.findByDepartmentCode(departmentCode);
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponseDtos.add(new EmployeeResponseDto(employee.getFirstName(), employee.getLastName(), employee.getEmail()));
        }
        return employeeResponseDtos;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Optional<EmployeeResponseDto> findByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee.getFirstName(), employee.getLastName(), employee.getEmail());
            return Optional.of(employeeResponseDto);
        }
        return Optional.empty();
    }


    /**
     * @param email
     * @param employeeRequestDto
     * @return
     */
    @Override
    public Optional<EmployeeResponseDto> updateEmployeePartially(String email, EmployeeRequestDto employeeRequestDto) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(email);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            if (employee.getFirstName() != null) {
                employee.setFirstName(employeeRequestDto.firstName());
            }
            if (employee.getLastName() != null) {
                employee.setLastName(employeeRequestDto.lastName());
            }
            if (employee.getDepartmentCode() != null) {
                employee.setDepartmentCode(employeeRequestDto.departmentCode());
            }
            //Save updated employee
            Employee updatedEmployee = employeeRepository.save(employee);
            //Convert to Dto and return
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(updatedEmployee.getFirstName(), updatedEmployee.getLastName(), updatedEmployee.getEmail());
            return Optional.of(employeeResponseDto);
        }
        return Optional.empty();
    }

    /**
     * @param employeeRequestDto
     * @return
     */
    @Override
    public Optional<EmployeeResponseDto> updateEmployee(String email, EmployeeRequestDto employeeRequestDto) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(email);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            //Update the employee details
            employee.setFirstName(employeeRequestDto.firstName());
            employee.setLastName(employeeRequestDto.lastName());
            employee.setDepartmentCode(employeeRequestDto.departmentCode());
            //Save updated employee
            Employee updatedEmployee = employeeRepository.save(employee);
            //Convert to Dto and return
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(updatedEmployee.getFirstName(), updatedEmployee.getLastName(), updatedEmployee.getEmail());
            return Optional.of(employeeResponseDto);
        }
        return Optional.empty();
    }

    /**
     * @param email
     */
    @Override
    public void deleteEmployee(String email) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(email);
        existingEmployee.ifPresent(employeeRepository::delete);
    }
}
