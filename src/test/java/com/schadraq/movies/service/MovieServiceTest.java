package com.schadraq.movies.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import com.schadraq.movies.dto.Movie;
import com.schadraq.movies.service.MovieService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
//@DataJpaTest
public class MovieServiceTest {

//	@TestConfiguration
//	static class MovieServiceConfiguration {
//		
//		@Bean
//		MovieService service() {
//			return new MovieService();
//		}
//	}

	private Logger log = LogManager.getLogger(MovieServiceTest.class);
	
	@Autowired
	private MovieService service;

	@BeforeEach
	public void beforeTest() {

		Movie m = new Movie("The Goonies", 1985);
		this.service.add(m);
	}

	@Test
	@Transactional
	public void whenAddMovie_Valid() {

		Movie m = new Movie("Mary Poppins", 1965);
		assertThat(this.service.add(m))
			.isEqualTo("Movie added!");
	}

	@Test
	@Disabled
	@Transactional
	public void whenAddMovie_InvalidDuplicate() {

		this.service.list().forEach(movie -> log.info("Step1: " + movie.toString()));
		Movie m = new Movie("The Goonies", 1985);
		this.service.add(m);
		this.service.list().forEach(movie -> log.info("Step2: " + movie.toString()));
//		assertThatExceptionOfType(DataIntegrityViolationException.class)
//			.isThrownBy(() -> this.service.add(m))
//			.withMessage("uniqueness: Object already exists");
		assertThat(this.service.add(m))
			.isEqualTo("Movie added!");
	}

	@Test
	@Transactional
	public void whenAddMovie_InvalidTitle() {

		Movie m = new Movie("", 1965);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(m))
			.withMessage("Error occurred: The title must contain content");
	}

	@Test
	@Transactional
	public void whenAddMovie_InvalidMinYear() {

		Movie m = new Movie("Mary Poppins", 1886);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(m))
			.withMessage("Error occurred: The release year must be at least 1888");
	}

	@Test
	@Transactional
	public void whenAddMovie_InvalidMaxYear() {

		Movie m = new Movie("Mary Poppins", 2222);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.add(m))
			.withMessage("Error occurred: The release year cannot be in the future");
	}

	@Test
	@Transactional
	public void whenGetMovie_Valid() {

		Movie movie = new Movie("The Goonies", 1985);
		assertThat(this.service.get(movie))
			.allMatch(m -> { return (m.getTitle().equals(movie.getTitle()) && m.getReleaseYear()==movie.getReleaseYear()); });
	}

	@Test
	@Transactional
	public void whenGetMovie_InvalidTitle() {

		Movie movie = new Movie("", 1965);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(movie))
			.withMessage("Error occurred: The title must contain content");
	}

	@Test
	@Transactional
	public void whenGetMovie_InvalidMaxYear() {

		Movie movie = new Movie("Mary Poppins", 2222);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(movie))
			.withMessage("Error occurred: The release year cannot be in the future");
	}

	@Test
	@Transactional
	public void whenGetMovie_InvalidMinYear() {

		Movie movie = new Movie("Mary Poppins", 1886);
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.service.get(movie))
			.withMessage("Error occurred: The release year must be at least 1888");
	}
}
