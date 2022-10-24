package com.example.employee.api.employeeapi.integration;

import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UpdateEmployeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EmployeeRepository repository;

    @Test
    public void updateEmployeeWhenEmailNotExistsStatusShouldBeOk() throws Exception {

        long employeeId = 1;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        this.repository.saveAndFlush(employee);

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe Jr",
                "john_doe_jr@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is(updateEmployeeDto.getEmployeeId().intValue())))
                .andExpect(jsonPath("$.firstName", Matchers.is(updateEmployeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(updateEmployeeDto.getLastName())))
                .andExpect(jsonPath("$.email", Matchers.is(updateEmployeeDto.getEmail())));

        var employeeOptional = repository.findById(employeeId);

        assertTrue(employeeOptional.isPresent());

        var updatedEmployee = employeeOptional.get();

        assertEquals(employeeId, updatedEmployee.getId());
        assertEquals(updateEmployeeDto.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(updateEmployeeDto.getLastName(), updatedEmployee.getLastName());
        assertEquals(updateEmployeeDto.getEmail(), updatedEmployee.getEmail());

    }

    @Test
    public void updateEmployeeWhenEmailIsUnchangedStatusShouldBeOk() throws Exception {
        long employeeId = 1;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        this.repository.saveAndFlush(employee);

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe Jr",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is(updateEmployeeDto.getEmployeeId().intValue())))
                .andExpect(jsonPath("$.firstName", Matchers.is(updateEmployeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(updateEmployeeDto.getLastName())))
                .andExpect(jsonPath("$.email", Matchers.is(updateEmployeeDto.getEmail())));

        var employeeOptional = repository.findById(employeeId);

        assertTrue(employeeOptional.isPresent());

        var updatedEmployee = employeeOptional.get();

        assertEquals(employeeId, updatedEmployee.getId());
        assertEquals(updateEmployeeDto.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(updateEmployeeDto.getLastName(), updatedEmployee.getLastName());
        assertEquals(updateEmployeeDto.getEmail(), updatedEmployee.getEmail());
    }

    @Test
    public void updateEmployeeWhenEmployeeNotExistsStatusShouldBeNotFound() throws Exception {

        var notExistentId = 100L;

        var updateEmployeeDto = new UpdateEmployeeDto(
                notExistentId,
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}", notExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    public void updateEmployeeWhenEmailAlreadyExistsStatusShouldBeUnprocessableEntity() throws Exception {

        var employeeId = 1L;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        var employeeWithTheSameMail = new Employee(
                2L,
                "Dude",
                "Joe",
                "dude_joe@mail.com");

        repository.saveAndFlush(employee);
        repository.saveAndFlush(employeeWithTheSameMail);

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe",
                "dude_joe@mail.com");


        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

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
