# Employees API
Simple REST API of a crud of employees made with Spring Boot and Mysql.

### Example of Controller
```java
public class EmployeesController {

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody CreateEmployeeDto dto) {
        var result = service.create(dto);
        return ResponseEntity.ok(result);
    }
}
```

### Example of Service
```java
public class EmployeeService {
    @Override
    public EmployeeDto create(CreateEmployeeDto dto) {
        var newEmployee = mapper.createEmployeeDtoToEmployee(dto);

        if (repository.exists(getEmailExample(dto.getEmail()))) {
            throw new DomainException("The email it's already registered.");
        }

        var createdEmployee = repository.saveAndFlush(newEmployee);
        return mapper.employeeToEmployeeDto(createdEmployee);
    }
}
```

### Example of Service Test
```java
public class CreateEmployeeTests {
    @Test
    public void createEmployeeWhenEmailNotExistsShouldReturnEmployeeDto() {

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var employeeDto = new EmployeeDto(
                1L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var employee = new Employee(
                1L,
                "John",
                "Doe",
                "john_doe@mail.com");

        var anyExample = ArgumentMatchers.<Example<Employee>> any();
        when(repository.exists(anyExample)).thenReturn(false);
        when(mapper.createEmployeeDtoToEmployee(createEmployeeDto)).thenReturn(employee);
        when(repository.saveAndFlush(employee)).thenReturn(employee);
        when(mapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        var result = service.create(createEmployeeDto);

        assertEquals(employeeDto, result);

    }
}
```

### Example of Validation Test
```java
public class CreateEmployeeTests {
    @Test
    public void createEmployeeWhenMinFirstNameLengthInvalidStatusShouldBeBadRequest() throws Exception {

        var createEmployeeDto = new CreateEmployeeDto(
                "a",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
```

### Example of Integration Test
```java
class CreateEmployeeTests {
    @Test
    public void createEmployeeWhenEmployeeEmailNotExistsStatusShouldBeOk() throws Exception {

        long expectedId = 1L;

        var createEmployeeDto = new CreateEmployeeDto(
                "John",
                "Doe",
                "john_doe@mail.com");

        var request = MockMvcRequestBuilders
                .post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEmployeeDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is((int) expectedId)))
                .andExpect(jsonPath("$.firstName", Matchers.is(createEmployeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(createEmployeeDto.getLastName())))
                .andExpect(jsonPath("$.email", Matchers.is(createEmployeeDto.getEmail())));

        var employeeOptional = repository.findById(expectedId);

        assertTrue(employeeOptional.isPresent());


        var newEmployee = employeeOptional.get();

        assertEquals(expectedId, newEmployee.getId());
        assertEquals(createEmployeeDto.getFirstName(), newEmployee.getFirstName());
        assertEquals(createEmployeeDto.getLastName(), newEmployee.getLastName());
        assertEquals(createEmployeeDto.getEmail(), newEmployee.getEmail());

    }
}
```


## Libraries
* Validation 
* JPA
* Test Containers
* Map Struct