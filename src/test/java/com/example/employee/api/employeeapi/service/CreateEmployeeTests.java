package com.example.employee.api.employeeapi.service;

import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.exception.DomainException;
import com.example.employee.api.employeeapi.model.mapper.EmployeeMapper;
import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateEmployeeTests {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void createEmployeeWhenEmailNotExistsShouldReturnEmployeeDto() {

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var employeeDto = new EmployeeDto(
                1L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var employee = new Employee(
                1L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var anyExample = ArgumentMatchers.<Example<Employee>> any();
        when(repository.exists(anyExample)).thenReturn(false);
        when(mapper.createEmployeeDtoToEmployee(createEmployeeDto)).thenReturn(employee);
        when(repository.saveAndFlush(employee)).thenReturn(employee);
        when(mapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        var result = service.create(createEmployeeDto);

        assertEquals(employeeDto, result);

    }

    @Test
    public void createEmployeeWhenEmailExistsShouldThrowDomainException() {
        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var anyExample = ArgumentMatchers.<Example<Employee>> any();
        when(repository.exists(anyExample)).thenReturn(true);

        assertThrows(DomainException.class, () -> service.create(createEmployeeDto));
    }

}
