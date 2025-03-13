package com.bright.repolayertesting.controller;

import com.bright.repolayertesting.dto.request.EmployeeRequestDto;
import com.bright.repolayertesting.dto.response.EmployeeResponseDto;
import com.bright.repolayertesting.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        List<EmployeeResponseDto> employeeResponseDtos = employeeService.getAllEmployees();
        return new ResponseEntity<>(employeeResponseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        Optional<EmployeeResponseDto> optionalEmployeeResponseDto = employeeService.createEmployee(employeeRequestDto);
        return optionalEmployeeResponseDto.map(employeeResponseDto -> new ResponseEntity<>(employeeResponseDto, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmail(@PathVariable String email) {
        Optional<EmployeeResponseDto> employeeResponseDto = employeeService.findByEmail(email);
        if (employeeResponseDto.isPresent()) {
            return new ResponseEntity<>(employeeResponseDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByFirstName(@PathVariable String firstName) {
        List<EmployeeResponseDto> employeeResponseDtos = employeeService.findByFirstName(firstName);
        if (employeeResponseDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employeeResponseDtos.getFirst(), HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable String email, @RequestBody EmployeeRequestDto employeeRequestDto) {
        Optional<EmployeeResponseDto> employeeResponseDto = employeeService.updateEmployee(email, employeeRequestDto);
        if (employeeResponseDto.isPresent()) {
            return new ResponseEntity<>(employeeResponseDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<EmployeeResponseDto> updateEmployeePartially(@PathVariable String email, @RequestBody EmployeeRequestDto employeeRequestDto) {
        Optional<EmployeeResponseDto> employeeResponseDto = employeeService.updateEmployeePartially(email, employeeRequestDto);
        if (employeeResponseDto.isPresent()) {
            return new ResponseEntity<>(employeeResponseDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String email) {
        employeeService.deleteEmployee(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
