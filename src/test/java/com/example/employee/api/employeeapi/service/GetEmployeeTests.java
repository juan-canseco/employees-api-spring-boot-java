package com.example.employee.api.employeeapi.service;

import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.exception.RecordNotFoundException;
import com.example.employee.api.employeeapi.model.mapper.EmployeeMapper;
import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetEmployeeTests {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void getEmployeeWhenEmployeeExistsShouldReturnEmployeeDto() {

        var employeeId = 1L;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        var employeeDto = new EmployeeDto(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(mapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        var result = service.getById(employeeId);
        assertEquals(employeeDto, result);
    }

    @Test
    public void getEmployeeWhenEmployeeNotExistsShouldThrowException() {

        var employeeId = 1L;

        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> service.getById(employeeId));
    }
}
