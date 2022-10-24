package com.example.employee.api.employeeapi.controller;

import com.example.employee.api.employeeapi.dto.CreateEmployeeDto;
import com.example.employee.api.employeeapi.dto.EmployeeDto;
import com.example.employee.api.employeeapi.dto.UpdateEmployeeDto;
import com.example.employee.api.employeeapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/employees")
@RestController
public class EmployeesController {
    private final EmployeeService service;

    @Autowired
    private EmployeesController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody CreateEmployeeDto dto) {
        var result = service.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable long employeeId) {
        var result = service.getById(employeeId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<EmployeeDto> update(@PathVariable long employeeId, @RequestBody @Valid UpdateEmployeeDto dto) {
        if (!dto.getEmployeeId().equals(employeeId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var result = service.edit(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<?> delete(@PathVariable long employeeId) {
        service.delete(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployeeDto>> getAll() {
        var result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity<>("Not valid due to validation error:" + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
