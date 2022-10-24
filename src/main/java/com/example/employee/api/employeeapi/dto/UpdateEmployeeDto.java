package com.example.employee.api.employeeapi.dto;

import lombok.*;
import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDto {
    @Min(1L)
    private Long employeeId;
    @Size(min = 2, max = 50)
    private String firstName;
    @Size(min = 2, max = 50)
    private String lastName;
    @Size(max = 75)
    @Email
    private String email;
}
