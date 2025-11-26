package com.schadraq.movies.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Entity
@Data
@Table(name = "person", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "birth_date" })
})
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotBlank(message = "Person name cannot be blank")
	private String name;

	// NOTE: Format must be YYYY-MM-DD
	@Column(name = "birth_date")
	@NotNull(message = "Person birthdate cannot be null")
	@PastOrPresent(message = "Person birthdate cannot be in the future")
	private LocalDate birthDate;

	public Person() {
		
	}

	public Person(String name, LocalDate birthDate) {
		this.name = name;
		this.birthDate = birthDate;
	}
}

