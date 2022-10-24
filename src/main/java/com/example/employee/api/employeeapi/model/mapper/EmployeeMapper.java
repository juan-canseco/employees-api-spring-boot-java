package com.example.employee.api.employeeapi.model.mapper;

import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.model.Employee;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EmployeeMapper {
    Employee createEmployeeDtoToEmployee(CreateEmployeeDto dto);
    EmployeeDto employeeToEmployeeDto(Employee employee);
}
