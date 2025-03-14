package com.bright.ems.repository;

import com.bright.ems.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        showSql = true,
        properties = {
                "spring.jpa.hibernate.ddl-auto=update"
        }
)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@gmail.com")
                .departmentCode("Compro")
                .build();
    }

    @AfterEach
    void tearDown() {
        System.out.println("clear up");
    }

    @Test
    @DisplayName("Test for save employee operation")
    void givenEmployee_whenSaved_thenReturnSavedEmployee() {
        // Given: An employee object is created
        // (Already initialized in setUp())

        // When: The employee is saved
        Employee savedEmployee = employeeRepository.save(employee);

        // Then: The saved employee should not be null and should have the expected first name
        assertNotNull(savedEmployee);
        assertEquals(employee.getFirstName(), savedEmployee.getFirstName());
        Assertions.assertThat(savedEmployee.getEmployeeId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test for get Employee list")
    void givenEmployees_whenFindAll_thenReturnList() {
        // Given: Multiple employees are saved in the repository
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@gmail.com")
                .departmentCode("Compro")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@gmail.com")
                .departmentCode("MBA")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // When: Retrieving all employees
        List<Employee> employeeList = employeeRepository.findAll();

        // Then: The list should contain all saved employees
        Assertions.assertThat(employeeList)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(employee1, employee2);
    }

    @Test
    @DisplayName("Test for get Employee by Email")
    void givenEmployee_whenFindByEmail_thenReturnEmployee() {
        // Given: An employee is saved in the repository
        employeeRepository.save(employee);

        // When: Searching for an employee by email
        Employee employeeByEmail = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(() -> new AssertionError("Employee not found"));

        // Then: The retrieved employee should have the expected first name
        assertEquals(employee.getFirstName(), employeeByEmail.getFirstName());
    }

    @Test
    @DisplayName("Test for get All Employee By First Name")
    void givenEmployees_whenFindByFirstName_thenReturnList() {
        // Given: Multiple employees with the same first name are saved
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@gmail.com")
                .departmentCode("Compro")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@gmail.com")
                .departmentCode("MBA")
                .build();
        Employee employee3 = Employee.builder()
                .firstName("Jane")
                .lastName("Thomson")
                .email("jane.thomson@gmail.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        // When: Searching for employees by first name "Jane"
        List<Employee> employeeList = employeeRepository.findByFirstName("Jane");

        // Then: The returned list should contain 2 employees with the first name "Jane"
        assertNotNull(employeeList);
        assertEquals(2, employeeList.size());
    }

    @Test
    @DisplayName("Test for update")
    void givenEmployee_whenUpdated_thenReturnUpdatedEmployee() {
        // Given: An employee is saved in the repository
        employeeRepository.save(employee);

        // When: Retrieving, updating, and saving the employee
        Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(() -> new AssertionError("Employee not found"));
        foundEmployee.setFirstName("Johny");
        employeeRepository.save(foundEmployee);

        // Then: The update should be persisted in the database
        Employee updatedEmployee = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(() -> new AssertionError("Updated Employee not found"));
        assertNotNull(updatedEmployee);
        assertEquals("Johny", updatedEmployee.getFirstName());
    }

}