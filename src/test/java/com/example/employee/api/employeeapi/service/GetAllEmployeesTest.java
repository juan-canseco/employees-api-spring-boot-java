package com.example.employee.api.employeeapi.service;

import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.model.mapper.EmployeeMapper;
import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllEmployeesTest {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void getAllEmployeesShouldReturnEmployees() {

        var employee1 = new Employee(1L, "John", "Doe", "john_doe@mail.com");
        var employee2 = new Employee(2L,"John", "Doe Jr", "john_doe_jr@mail.com");
        var employees = List.of(employee1, employee2);


        var employeeDto1 = new EmployeeDto(1L, "John", "Doe", "john_doe@mail.com");
        var employeeDto2 = new EmployeeDto(2L,"John", "Doe Jr", "john_doe_jr@mail.com");
        var employeesDto = List.of(employeeDto1, employeeDto2);

        when(repository.findAll()).thenReturn(employees);
        when(mapper.employeeToEmployeeDto(employee1)).thenReturn(employeeDto1);
        when(mapper.employeeToEmployeeDto(employee2)).thenReturn(employeeDto2);

        var result = service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employeesDto, result);
    }

}
