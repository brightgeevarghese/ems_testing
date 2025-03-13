package com.bright.ems.controller;

import com.bright.ems.dto.request.EmployeeRequestDto;
import com.bright.ems.dto.response.EmployeeResponseDto;
import com.bright.ems.service.EmployeeService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("Test for get all employees")
    void whenGetAllEmployees_thenReturnResponses() throws Exception {
        //Given
        List<EmployeeResponseDto> employeeResponseDtos = Arrays.asList(
                new EmployeeResponseDto("John", "Smith", "Compro"),
                new EmployeeResponseDto("Jane", "Smith", "MBA")
        ) ;
        //Set mockito behaviour
        Mockito.when(employeeService.getAllEmployees()).thenReturn(employeeResponseDtos);
        //When
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/employees")
        )
                //then
                .andExpectAll(
                        MockMvcResultMatchers.content().json(new Gson().toJson(employeeResponseDtos)),
                        MockMvcResultMatchers.status().isOk()
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void givenEmployeeRequest_whenCreate_thenReturnSavedResponse() throws Exception {
        //Given
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto("John", "Smith", "john.smith@gmail.com", "Compro");
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto("John", "Smith", "Compro");
        //Set mockito behaviour
        Mockito.when(employeeService.createEmployee(employeeRequestDto)).thenReturn(Optional.of(employeeResponseDto));
        //When
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(employeeRequestDto))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new Gson().toJson(employeeResponseDto)));
    }

    @Test
    @DisplayName("Test for create Employee with invalid EmployeeRequestDto")
    void givenEmployeeRequest_whenCreate_thenReturnBadRequest() throws Exception {
        //Given
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto("John", "Smith", "john.smith@gmail.com", "Compro");
        Mockito.when(employeeService.createEmployee(employeeRequestDto)).thenReturn(Optional.empty());
        //When
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeRequestDto))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("Test for get existing Employee by email")
    void givenExistingEmail_whenFind_thenReturnEmployeeResponse() throws Exception {
        //Given
        String email = "john.smith@gmail.com";
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto("John", "Smith", "Compro");
        //Set Mockito behaviour
        Mockito.when(employeeService.findByEmail(email)).thenReturn(Optional.of(employeeResponseDto));
        //When
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/employees/email/{email}", email)
        )
                .andDo(MockMvcResultHandlers.print())
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new Gson().toJson(employeeResponseDto)));
    }

    @Test
    @DisplayName("Test for get non-existing employee by email")
    void givenNonExistingEmail_whenFind_thenReturnNotFound() throws Exception {
        //Given
        String email = "john.smith@gmail.com";
        Mockito.when(employeeService.findByEmail(email)).thenReturn(Optional.empty());
        //When
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/employees/email/{email}", email)
        )
                .andDo(MockMvcResultHandlers.print())
                //Then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }


    @Test
    void givenExistingEmail_whenDelete_thenReturnNoContent() throws Exception {
        //Given
        String email = "john.smith@gmail.com";
        Mockito.doNothing().when(employeeService).deleteEmployee(email);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/employees/{email}", email)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void givenNonExistingEmail_whenDelete_thenReturnNotFound() throws Exception {
        String email = "john.smith@gmail.com";
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(employeeService).deleteEmployee(email);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/employees/{email}", email)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenExistingEmail_whenUpdate_thenReturnEmployeeResponse() throws Exception {
        // Given: An existing employee and an employeeRequestDto for updating
        String email = "john.smith@gmail.com";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto("John", "Smith", "john.smith@gmail.com", "Compro");
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto("John", "Smith", "Compro");

        Mockito.when(employeeService.updateEmployee(email, employeeRequestDto)).thenReturn(Optional.of(employeeResponseDto));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/employees/{email}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeRequestDto))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}