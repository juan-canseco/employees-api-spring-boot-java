package com.example.employee.api.employeeapi.validation;

import com.example.employee.api.employeeapi.controller.EmployeesController;
import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeesController.class)
public class CreateEmployeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private EmployeeService service;
    @Test
    public void createEmployeeWhenAllFieldsAreValidStatusShouldBeOk() throws Exception {
        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void createEmployeeWhenMinFirstNameLengthInvalidStatusShouldBeBadRequest() throws Exception {

        var createEmployeeDto = new CreateEmployeeDto(
                "a",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createEmployeeWhenMaxFirstNameLengthInvalidStatusShouldBeBadRequest() throws Exception {
        var createEmployeeDto = new CreateEmployeeDto(
          "Extra Looooooooooooooooooooooooong Nameeeeeeeeeeeee",
          "Doe",
          "john_doe@mail.com"
        );

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployeeWhenMinLastNameLengthInvalidStatusShouldBeBadRequest() throws Exception {
        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "D",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployeeWhenMaxLastNameLengthInvalidStatusShouldBeBadRequest() throws Exception {

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Very Very Ver Long LastNameeeeeeeeeeeeeeeeeeeeeeeee",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployeeWhenEmailIsInvalidStatusShouldBeBadRequest() throws Exception {
        var createEmployeeDto = new CreateEmployeeDto(
          "John",
          "Doe",
          "some_invalid_email...@mail...com"
        );

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployeeWhenEmailMaxLengthIsInvalidStatusShouldBeBadRequest() throws Exception {
        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe_alias_the_man_who_knows_how_to_meet_ladies_fucking_giga_ch@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
