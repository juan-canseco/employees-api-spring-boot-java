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
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetAllEmployeesTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository repository;

    @Test
    public void getAllEmployeesWhenEmployeesExistStatusShouldBeOkAndShouldReturnList() throws Exception {

        var employees = List.of(
                new Employee(1L, "John", "Doe", "john_doe@mail.com"),
                new Employee(2L, "Joe", "Down", "joe_down@mail.com")
        );

        repository.saveAllAndFlush(employees);

        var request = MockMvcRequestBuilders
                .get("/api/v1/employees/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));

    }

    @Test
    public void getAllEmployeesWhenListIsEmptyStatusShouldBeOkAndListShouldBeEmpty() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/v1/employees/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
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
