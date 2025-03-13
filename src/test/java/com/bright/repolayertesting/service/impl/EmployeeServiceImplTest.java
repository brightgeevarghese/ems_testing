package com.bright.repolayertesting.service.impl;

import com.bright.repolayertesting.dto.request.EmployeeRequestDto;
import com.bright.repolayertesting.dto.response.EmployeeResponseDto;
import com.bright.repolayertesting.model.Employee;
import com.bright.repolayertesting.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("Test for save Employee")
    void givenEmployeeRequest_whenCreate_thenReturnSavedResponse() {
        //Given
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto("John", "Smith", "john.smith@gmail.com", "Compro");
        Employee savedEmployee = new Employee("John", "Smith", "john.smith@gmail.com", "Compro");
        //Define the behaviour of the mocked repository
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(savedEmployee);
        //When
        Optional<EmployeeResponseDto> employeeResponseDto = employeeService.createEmployee(employeeRequestDto);
        //Then
        assertTrue(employeeResponseDto.isPresent());
        assertEquals("John", employeeResponseDto.get().firstName());
    }

    @Test
    @DisplayName("Test for find all employees by FirstName")
    void givenFirstName_whenFind_thenReturnEmployeeResponses() {
        //Given
        String firstName = "John";
        List<Employee> employees = List.of(
                Employee.builder().firstName("John").lastName("Smith").build(),
                Employee.builder().firstName("John").lastName("Thomas").build()
        );
        //Define what the mock should do when the findByFirstName method is called
        Mockito.when(employeeRepository.findByFirstName(firstName)).thenReturn(employees);
        //When
        List<EmployeeResponseDto> employeeResponseDtos = employeeService.findByFirstName(firstName);
        //Then
        assertFalse(employeeResponseDtos.isEmpty());
        assertEquals("John", employeeResponseDtos.getFirst().firstName());
    }

    @Test
    @DisplayName("Test for get all employees by last name")
    void givenLastName_whenFind_thenReturnEmployeeResponses() {
        //Given
        String lastName = "Smith";
        List<Employee> employees = List.of(
                Employee.builder().firstName("John").lastName("Smith").build(),
                Employee.builder().firstName("Jane").lastName("Smith").build()
        );
        //Define what the mock should do when findByLastName method is called
        Mockito.when(employeeRepository.findByLastName(lastName)).thenReturn(employees);
        //When
        List<EmployeeResponseDto> employeeResponseDtos = employeeService.findByLastName(lastName);
        //Then
        assertFalse(employeeResponseDtos.isEmpty());
        assertEquals(2, employeeResponseDtos.size());
        Mockito.verify(employeeRepository, Mockito.times(1)).findByLastName(lastName);
    }

    @Test
    @DisplayName("Test for deleting an employee by using email")
    void givenEmail_whenDelete_thenEmployeeDeleted() {
        //Given
        String email = "john.smith@gmail.com";
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Smith")
                .email(email)
                .build();

        Mockito.when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeRepository).delete(employee);

        //When
        employeeService.deleteEmployee(email);
        //Then
//        Mockito.verify(employeeRepository, Mockito.times(1)).findByEmail(email);
//        Mockito.verify(employeeRepository, Mockito.times(1)).delete(employee);
        //Mockito.times(1) is redundant because .verify() already checks for a single call by default.
        Mockito.verify(employeeRepository).findByEmail(email);
        Mockito.verify(employeeRepository).delete(employee);
    }

    @Test
    @DisplayName("Test for updating an employee")
    void givenValidRequest_whenUpdate_thenReturnUpdatedResponse() {
        //Given
        String email = "john.smith@gmail.com";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto("Jane", "Thomas", email, "MBA");
        Employee existingEmployee = Employee.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@gmail.com")
                .departmentCode("Compro")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Thomas")
                .email("john.smith@gmail.com")
                .departmentCode("MBA")
                .build();
        Mockito.when(employeeRepository.findByEmail("john.smith@gmail.com")).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(updatedEmployee);
        //When
        Optional<EmployeeResponseDto> employeeResponseDto = employeeService.updateEmployee(email, employeeRequestDto);
        //Then
        assertTrue(employeeResponseDto.isPresent());
//        Mockito.verify(employeeRepository, Mockito.times(1)).findByEmail(email);
//        Mockito.verify(employeeRepository, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verify(employeeRepository).findByEmail(email);
        Mockito.verify(employeeRepository).save(Mockito.any(Employee.class));
    }
}