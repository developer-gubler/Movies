package com.schadraq.movies.dto;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

	Optional<Person> findByNameAndBirthDate(String title, LocalDate birthDate);
}
