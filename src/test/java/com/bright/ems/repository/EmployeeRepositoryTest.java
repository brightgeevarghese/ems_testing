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
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee);
        assertEquals(employee.getFirstName(), savedEmployee.getFirstName());
        Assertions.assertThat(savedEmployee.getEmployeeId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test for get Employee list")
    void givenEmployees_whenFindAll_thenReturnList() {
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
        List<Employee> employeeList = employeeRepository.findAll();
        Assertions.assertThat(employeeList)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(employee1, employee2);
//        assertNotNull(employeeList);
//        assertEquals(2, employeeList.size());
//        Assertions.assertThat(employeeList.getFirst().getEmployeeId()).isEqualTo(employee1.getEmployeeId());
    }

    @Test
    @DisplayName("Test for get Employee by Email")
    void givenEmployee_whenFindByEmail_thenReturnEmployee() {
        employeeRepository.save(employee);
        Employee employeeByEmail = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(AssertionError::new);
        assertEquals(employee.getFirstName(), employeeByEmail.getFirstName());
    }

    @Test
    @DisplayName("Test for get All Employee By First Name")
    void givenEmployees_whenFindByFirstName_thenReturnList() {
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
        List<Employee> employeeList = employeeRepository.findByFirstName("Jane");
        assertNotNull(employeeList);
        assertEquals(2, employeeList.size());
    }

    @Test
    @DisplayName("Test for update")
    void givenEmployee_whenUpdated_thenReturnUpdatedEmployee() {
        employeeRepository.save(employee);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(optionalEmployee.isPresent()) {
            Employee foundEmployee = optionalEmployee.get();
            foundEmployee.setFirstName("Johny");
            Employee updatedEmployee = employeeRepository.save(foundEmployee);
            assertNotNull(updatedEmployee);
            assertEquals("Johny", updatedEmployee.getFirstName());
        }
    }

}