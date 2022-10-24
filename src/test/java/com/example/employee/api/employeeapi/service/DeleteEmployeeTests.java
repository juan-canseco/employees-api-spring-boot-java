package com.example.employee.api.employeeapi.service;

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
public class DeleteEmployeeTests {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void deleteEmployeeWhenEmployeeExistsShouldDelete() {

        var employeeId = 1L;

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(repository).delete(employee);

        service.delete(employeeId);

        verify(repository, times(1)).delete(employee);
    }

    @Test
    public void deleteEmployeeWhenEmployeeNotExistsShouldThrowException() {

        var employeeId = 1L;

        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> service.delete(employeeId));
    }
}
