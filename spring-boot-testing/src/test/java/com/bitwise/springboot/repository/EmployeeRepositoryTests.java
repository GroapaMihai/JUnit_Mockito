package com.bitwise.springboot.repository;

import com.bitwise.springboot.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
            .firstName("Ramesh")
            .lastName("Fadatare")
            .email("ramesh.fadatare@gmail.com")
            .build();
    }

    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployee_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get all employees operation")
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // given - precondition or setup

        Employee employee2 = Employee.builder()
            .firstName("John")
            .lastName("Cena")
            .email("john.cena@gmail.com")
            .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or the behaviour that we are going to test
        List<Employee> employees = employeeRepository.findAll();

        // then - verify the output
        Assertions.assertThat(employees).isNotNull();
        Assertions.assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for get employee by id operation")
    public void givenEmployee_whenFindById_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Optional<Employee> employeeDbOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        Assertions.assertThat(employeeDbOptional.get()).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Optional<Employee> employeeDbOptional = employeeRepository.findByEmail(employee.getEmail());

        // then - verify the output
        Assertions.assertThat(employeeDbOptional.get()).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for update employee operation")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Ram");
        savedEmployee.setEmail("ram@gmail.com");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
    }

    @Test
    @DisplayName("JUnit test for delete employee operation")
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeDbOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        Assertions.assertThat(employeeDbOptional).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for custom query using JPQL with index parameters")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for custom query using native SQL with index")
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for custom query using native SQL with named params")
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }
}
