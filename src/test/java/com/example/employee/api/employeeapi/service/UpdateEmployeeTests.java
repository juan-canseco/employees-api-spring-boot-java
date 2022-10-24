package com.example.employee.api.employeeapi.service;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
import com.example.employee.api.employeeapi.exception.DomainException;
import com.example.employee.api.employeeapi.exception.RecordNotFoundException;
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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateEmployeeTests {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void updateEmployeeWhenEmailNotExistsShouldReturnEmployeeDto() {

        var employeeId = 1L;

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe",
                "john_doe_crack@mail.com");

        var employeeDto = new EmployeeDto(
                employeeId,
                "John",
                "Doe",
                "john_doe_crack@mail.com");

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        var employeeResult = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe_crack@mail.com");

        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(repository.save(employee)).thenReturn(employeeResult);
        when(mapper.employeeToEmployeeDto(employeeResult)).thenReturn(employeeDto);

        var result = service.edit(updateEmployeeDto);

        assertNotNull(result);
        assertEquals(employeeDto, result);
    }

    @Test
    public void updateEmployeeWhenEmailExistsShouldThrowException() {

        var employeeId = 1L;

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe",
                "john_doe_crack@mail.com");

        var employee = new Employee(
                employeeId,
                "John",
                "Doe",
                "john_doe@mail.com");

        var anyExample = ArgumentMatchers.<Example<Employee>> any();
        when(repository.exists(anyExample)).thenReturn(true);
        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));

        assertThrows(DomainException.class, () -> service.edit(updateEmployeeDto));

    }

    @Test
    public void updateEmployeeWhenEmployeeNotExistsShouldThrowException() {

        var employeeId = 1L;

        var updateEmployeeDto = new UpdateEmployeeDto(
                employeeId,
                "John",
                "Doe",
                "john_doe_crack@mail.com");

        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> service.edit(updateEmployeeDto));


    }

}
