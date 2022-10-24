package com.example.employee.api.employeeapi.validation;

import com.example.employee.api.employeeapi.controller.EmployeesController;
import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
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
public class UpdateEmployeeTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeService service;

    @Test
    public void updateEmployeeWhenAllFieldsAreValidStatusShouldBeOk() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void updateEmployeeWhenEmployeeIdIsLessThanOneStatusShouldBeBadRequest() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                0L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmployeeWhenMinFirstNameLengthInvalidStatusShouldBeBadRequest() throws Exception {

        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "a",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    public void updateEmployeeWhenMaxFirstNameLengthInvalidStatusShouldBeBadRequest() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "Extra Looooooooooooooooooooooooong Nameeeeeeeeeeeee",
                "Doe",
                "john_doe@mail.com"
        );

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmployeeWhenMinLastNameLengthInvalidStatusShouldBeBadRequest() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "John",
                "D",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmployeeWhenMaxLastNameLengthInvalidStatusShouldBeBadRequest() throws Exception {

        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "John",
                "Very Very Ver Long LastNameeeeeeeeeeeeeeeeeeeeeeeee",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmployeeWhenEmailIsInvalidStatusShouldBeBadRequest() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "John",
                "Doe",
                "some_invalid_email...@mail...com"
        );

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEmployeeWhenEmailMaxLengthIsInvalidStatusShouldBeBadRequest() throws Exception {
        var updateEmployeeDto = new UpdateEmployeeDto(
                1L,
                "John",
                "Doe",
                "john_doe_alias_the_man_who_knows_how_to_meet_ladies_fucking_giga_ch@mail.com");

        var request = MockMvcRequestBuilders
                .put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
