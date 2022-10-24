package com.example.employee.api.employeeapi.persistence;

import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository repository;
    private List<Employee> getUsers() {
        return List.of(
                new Employee(1L, "John", "Doe", "john_doe@mail.com"),
                new Employee(2L, "Jane", "Doe", "jane_doe@mail.com"),
                new Employee(3L, "Gary","Sue", "gary_sue@mail.com"));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Transactional
    @Test
    public void createEmployeeShouldReturnEmployee() {

        var employee = new Employee();

        employee.setFirstName("Mary");
        employee.setLastName("Sue");
        employee.setEmail("mary_sue@mail.com");

        var result = repository.saveAndFlush(employee);

        assertTrue(result.getId() > 0L);
    }

    @Transactional
    @Test
    public void getEmployeeByShouldReturnEmployee() {
        repository.saveAllAndFlush(getUsers());
        var employee = repository.findById(1L);
        assertTrue(employee.isPresent());
    }

    @Transactional
    @Test
    public void updateEmployeeShouldReturnEmployee() {

        repository.saveAllAndFlush(getUsers());

        var newFirstName = "Johny";
        var newLastName = "Downs";
        var newEmail = "johny_downs@mail.com";

        var employeeOptional = repository.findById(1L);
        var employee = employeeOptional.get();

        employee.setEmail(newEmail);
        employee.setFirstName(newFirstName);
        employee.setLastName(newLastName);

        var result = repository.save(employee);

        assertEquals(newFirstName, result.getFirstName());
        assertEquals(newLastName, result.getLastName());
        assertEquals(newEmail, result.getEmail());
    }

    @Test
    public void deleteEmployee() {
        repository.saveAllAndFlush(getUsers());
        repository.deleteById(3L);
        var employees = repository.findAll();
        assertEquals(2, employees.size());
    }

    @Transactional
    @Test
    public void findAllEmployeesShouldReturnEmployeesList() {
        repository.saveAllAndFlush(getUsers());
        var result = repository.findAll();
        assertEquals(3, result.size());
    }

    @Transactional
    @Test
    public void employeeExistsByEmailWhenExistsShouldReturnTrue() {

        repository.saveAllAndFlush(getUsers());

        var emailMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("email", ignoreCase());

        var emailProbe = new Employee();
        emailProbe.setEmail("john_doe@mail.com");

        var emailExample = Example.of(emailProbe, emailMatcher);

        var result = repository.exists(emailExample);

        assertTrue(result);
    }


    @Container
    static MySQLContainer database = new MySQLContainer("mysql:latest")
            .withDatabaseName("employees_db")
            .withPassword("admin1234");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }
}
