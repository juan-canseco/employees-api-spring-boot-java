package com.example.employee.api.employeeapi.service;

import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
import com.example.employee.api.employeeapi.exception.DomainException;
import com.example.employee.api.employeeapi.exception.RecordNotFoundException;
import com.example.employee.api.employeeapi.model.mapper.EmployeeMapper;
import com.example.employee.api.employeeapi.model.Employee;
import com.example.employee.api.employeeapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EmployeeDto create(CreateEmployeeDto dto) {
        var newEmployee = mapper.createEmployeeDtoToEmployee(dto);

        if (repository.exists(getEmailExample(dto.getEmail()))) {
            throw new DomainException("The email it's already registered.");
        }

        var createdEmployee = repository.saveAndFlush(newEmployee);
        return mapper.employeeToEmployeeDto(createdEmployee);
    }


    private Example<Employee> getEmailExample(String email) {
        var emailMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("email", ignoreCase());

        var emailProbe = new Employee();
        emailProbe.setEmail(email);

        return Example.of(emailProbe, emailMatcher);
    }

    @Override
    public EmployeeDto getById(Long employeeId) {
        var employee = repository
                .findById(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("The Employee with the Id %d was not found." ));
        return mapper.employeeToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto edit(UpdateEmployeeDto dto) {

        var employee = repository
                .findById(dto.getEmployeeId())
                .orElseThrow(() -> new RecordNotFoundException("The Employee with the Id %d was not found."));

        if (!employee.getEmail().equals(dto.getEmail())) {
            if (repository.exists(getEmailExample(dto.getEmail()))) {
                throw new DomainException("The email it's already registered.");
            }
        }

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());

        var result = repository.save(employee);

        return mapper.employeeToEmployeeDto(result);
    }

    @Override
    public void delete(Long employeeId) {
        var employee = repository
                .findById(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("The Employee with the Id %d was not found."));
        repository.delete(employee);
    }

    @Override
    public List<EmployeeDto> getAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }
}
