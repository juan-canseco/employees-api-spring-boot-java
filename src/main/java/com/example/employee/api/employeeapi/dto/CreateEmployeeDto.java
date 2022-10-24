package com.example.employee.api.employeeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {
    @Size(min =  2, max = 50)
    private String firstName;
    @Size(min = 2, max = 50)
    private String lastName;
    @Email
    @Size(max = 75)
    private String email;
}

