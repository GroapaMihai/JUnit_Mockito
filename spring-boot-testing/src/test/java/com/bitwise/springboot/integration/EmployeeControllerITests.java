package com.bitwise.springboot.integration;

import com.bitwise.springboot.model.Employee;
import com.bitwise.springboot.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class uses locally installed MySQL to run the tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    // Needed to clear the records from the database
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();

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
            .firstName("Tony")
            .lastName("Stark")
            .email("tony.stark@gmail.com")
            .build();
        List<Employee> employeeList = List.of(employee, employee1);

        employeeRepository.saveAll(employeeList);

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
        Employee savedEmployee = employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(savedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(savedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(savedEmployee.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for get employee by id not found")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        Long employeeId = 1L;

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test for update employee by id REST API")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadvah")
                .email("ram@gmail.com")
                .build();

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
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

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 0L)
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
        Employee savedEmployee = employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
