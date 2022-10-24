package com.example.employee.api.employeeapi.integration;

import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CreateEmployeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EmployeeRepository repository;

    @Test
    public void createEmployeeWhenEmployeeEmailNotExistsStatusShouldBeOk() throws Exception {

        long expectedId = 1L;

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is((int) expectedId)))
                .andExpect(jsonPath("$.firstName", Matchers.is(createEmployeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(createEmployeeDto.getLastName())))
                .andExpect(jsonPath("$.email", Matchers.is(createEmployeeDto.getEmail())));

        var employeeOptional = repository.findById(expectedId);

        assertTrue(employeeOptional.isPresent());


        var newEmployee = employeeOptional.get();

        assertEquals(expectedId, newEmployee.getId());
        assertEquals(createEmployeeDto.getFirstName(), newEmployee.getFirstName());
        assertEquals(createEmployeeDto.getLastName(), newEmployee.getLastName());
        assertEquals(createEmployeeDto.getEmail(), newEmployee.getEmail());

    }

    @Test
    public void createEmployeeWhenEmailExistsStatusShouldBeUnprocessable() throws Exception {

        var alreadyExistEmailEmployee = new Employee(1L, "John", "Doe", "john_doe@mail.com");
        repository.saveAndFlush(alreadyExistEmailEmployee);

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe JR",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
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
