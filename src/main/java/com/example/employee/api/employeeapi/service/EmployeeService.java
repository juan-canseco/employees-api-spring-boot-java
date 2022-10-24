package com.example.employee.api.employeeapi.service;
import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
import java.util.List;

public interface EmployeeService {
    EmployeeDto getById(Long employeeId);
    List<EmployeeDto> getAll();
    EmployeeDto create(CreateEmployeeDto dto);
    EmployeeDto edit(UpdateEmployeeDto dto);
    void delete(Long employeeId);
}
