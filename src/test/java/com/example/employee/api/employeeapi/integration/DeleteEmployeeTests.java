package com.example.employee.api.employeeapi.integration;

import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DeleteEmployeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository repository;

    @Test
    public void deleteEmployeeWhenEmployeeExistsStatusShouldBeOk() throws Exception {

        var employeeId = 1L;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        repository.saveAndFlush(employee);

        var request = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var employeeExists = repository.existsById(employeeId);
        assertFalse(employeeExists);

    }

    @Test
    public void deleteEmployeeWhenEmployeeNotExistsStatusShouldNotFound() throws Exception {

        var employeeId = 1L;

        var request = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @After
    public void clearAll() {
        repository.deleteAll();
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
