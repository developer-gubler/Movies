package com.schadraq.movies.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;

import com.schadraq.movies.dto.Person;
import com.schadraq.movies.service.PersonService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
//@DataJpaTest
public class PersonServiceTest {

//	@TestConfiguration
//	static class MovieServiceConfiguration {
//		
//		@Bean
//		MovieService service() {
//			return new MovieService();
//		}
//	}

	@Autowired
	private PersonService service;

	@BeforeEach
	public void beforeTest() {

		Person p = new Person("Mickey Mouse", LocalDate.of(1928,11,28));
		this.service.add(p);
	}

	@Test
	@Transactional
	public void whenAddPerson_Valid() {

		Person p = new Person("Donald Duck", LocalDate.of(1934, 6, 9));
		assertThat(this.service.add(p))
			.isEqualTo("Person added!");
	}

	@Test
	@Transactional
	public void whenAddPerson_InvalidName() {

		Person p = new Person(null, LocalDate.of(1934, 6, 9));
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(p))
			.withMessage("Error occurred: Person name cannot be blank");
	}

	@Test
	@Transactional
	public void whenAddPerson_InvalidNullBirthdate() {

		Person p = new Person("Donald Duck", null);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(p))
			.withMessage("Error occurred: Person birthdate cannot be null");
	}

	@Test
	@Transactional
	public void whenAddPerson_InvalidFutureBirthdate() {

		Person p = new Person("Donald Duck", LocalDate.of(2525, 6, 9));
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(p))
			.withMessage("Error occurred: Person birthdate cannot be in the future");
	}

	@Test
	@Transactional
	public void whenGetPerson_Valid() {

		Person p = new Person("Mickey Mouse", LocalDate.of(1928,11,28));
		assertThat(this.service.get(p))
			.allMatch(person -> { return (person.getName().equals(p.getName()) && person.getBirthDate().isEqual(p.getBirthDate())); });
	}

	@Test
	@Transactional
	public void whenGetPerson_InvalidName() {

		Person p = new Person(null, LocalDate.of(1934, 6, 9));
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(p))
			.withMessage("Error occurred: Person name cannot be blank");
	}

	@Test
	@Transactional
	public void whenGetPerson_InvalidNullBirthdate() {

		Person p = new Person("Donald Duck", null);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(p))
			.withMessage("Error occurred: Person birthdate cannot be null");
	}

	@Test
	@Transactional
	public void whenGetPerson_InvalidFutureBirthdate() {

		Person p = new Person("Donald Duck", LocalDate.of(2525, 6, 9));
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(p))
			.withMessage("Error occurred: Person birthdate cannot be in the future");
	}
}
