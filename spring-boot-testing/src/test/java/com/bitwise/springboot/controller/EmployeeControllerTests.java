package com.bitwise.springboot.controller;

import com.bitwise.springboot.model.Employee;
import com.bitwise.springboot.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class EmployeeControllerTests {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
            .id(1L)
            .firstName("Ramesh")
            .lastName("Fadatare")
            .email("ramesh.fadatare@gmail.com")
            .build();
    }

    @Test
    @DisplayName("JUnit test for create employee REST API")
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
            .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for get all employees REST API")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
            .id(2L)
            .firstName("Tony")
            .lastName("Stark")
            .email("tony.stark@gmail.com")
            .build();

        given(employeeService.getAllEmployees())
            .willReturn(List.of(employee, employee1));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    @DisplayName("JUnit test for get employee by id REST API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId))
            .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for get employee by id not found")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        given(employeeService.getEmployeeById(anyLong()))
            .willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", anyLong()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test for update employee by id REST API")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
            .id(employeeId)
            .firstName("Ram")
            .lastName("Jadvah")
            .email("ram@gmail.com")
            .build();

        given(employeeService.getEmployeeById(employeeId))
            .willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
            .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for update employee by id when not found REST API")
    public void givenInvalidEmployeeId_whenUpdateEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        Employee updatedEmployee = Employee.builder()
            .firstName("Ram")
            .lastName("Jadvah")
            .email("ram@gmail.com")
            .build();

        given(employeeService.getEmployeeById(anyLong()))
            .willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", anyLong())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test for delete employee by id REST API")
    public void givenEmployeeId_whenDeleteEmployee_thenRemoveEmployee() throws Exception {
        // given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployee(1L);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1L));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
