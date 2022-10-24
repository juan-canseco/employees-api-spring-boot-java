package com.example.employee.api.employeeapi.integration;

import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.hamcrest.Matchers;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetEmployeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository repository;

    @Test
    public void getEmployeeByIdWhenEmployeeExistsStatusShouldBeOk() throws Exception {

        var employeeId = 1L;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        repository.saveAndFlush(employee);

        var request = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is((int)employeeId)))
                .andExpect(jsonPath("$.firstName", Matchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", Matchers.is(employee.getEmail())));
    }

    @Test
    public void getEmployeeByIdWhenEmployeeNotExistsStatusShouldBeNotFound() throws Exception {

        var employeeId = 1L;

        var request = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

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
