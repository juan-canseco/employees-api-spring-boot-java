package com.example.employee.api.employeeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	// https://medium.com/globant/mapstruct-advanced-concepts-and-dependency-injection-46f28af54e33
	// https://reflectoring.io/bean-validation-with-spring-boot/#validating-jpa-entities
	// https://codeburst.io/spring-boot-rest-microservices-best-practices-2a6e50797115
	// https://reflectoring.io/bean-validation-anti-patterns/#anti-pattern-1-validating-only-in-the-persistence-layer
	// Curse origin
	// https://www.youtube.com/watch?v=o_HV_FCs-Z0
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
