package com.example.employee.api.employeeapi.model;

import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", length = 50, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 75, nullable = false, unique = true)
    private String email;
}
