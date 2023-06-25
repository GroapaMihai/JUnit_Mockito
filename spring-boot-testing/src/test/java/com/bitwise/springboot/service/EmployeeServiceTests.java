package com.bitwise.springboot.service;

import com.bitwise.springboot.exception.ResourceNotFoundException;
import com.bitwise.springboot.model.Employee;
import com.bitwise.springboot.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// This annotation tells Mockito that we are using annotations (@mock, @InjectMocks) to mock the dependencies
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        // Replaced by @Mock
        // employeeRepository = Mockito.mock(EmployeeRepository.class);

        // Replaced by @InjectMocks
        // employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
            .id(1L)
            .firstName("Ramesh")
            .lastName("Fadatare")
            .email("ramesh.fadatare@gmail.com")
            .build();
    }

    @Test
    @DisplayName("JUnit test for save employee method")
    public void givenEmployee_whenSaveEmployee_thenReturnEmployee() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
            .willReturn(Optional.empty());
        given(employeeRepository.save(employee))
            .willReturn(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for save employee method which throws exception")
    public void givenEmployeeWithExistingEmail_whenSaveEmployee_thenThrowException() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
            .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("JUnit test for get all employees method")
    public void givenEmployeeList_whenGetAll_thenReturnEmployeeList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
            .id(2L)
            .firstName("Tony")
            .lastName("Stark")
            .email("tony.stark@gmail.com")
            .build();

        given(employeeRepository.findAll())
            .willReturn(List.of(employee, employee1));

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList).asList().hasSize(2);
    }

    @Test
    @DisplayName("JUnit test for get all employees method (negative scenario)")
    public void givenEmptyEmployeeList_whenGetAll_thenReturnEmptyEmployeeList() {
        // given - precondition or setup
        given(employeeRepository.findAll())
            .willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList).asList().hasSize(0);
    }

    @Test
    @DisplayName("JUnit test for find employee by id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
            .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(1L).get();

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("JUnit test for update employee")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        given(employeeRepository.save(employee))
            .willReturn(employee);
        employee.setFirstName("Ram");

        // when - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
    }

    @Test
    @DisplayName("JUnit test for delete employee")
    public void givenEmployeeId_whenDeleteEmployeeById_thenDeleteEmployee() {
        // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(1L);

        // when - action or the behaviour that we are going to test
        employeeService.deleteEmployee(1L);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
