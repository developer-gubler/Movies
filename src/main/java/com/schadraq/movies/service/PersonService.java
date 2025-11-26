package com.schadraq.movies.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.schadraq.movies.dto.Person;
import com.schadraq.movies.dto.PersonRepository;

@Service
public class PersonService extends EntityService<Person, PersonRepository> {

	@Override
	public List<Person> get(Person person) {

		return get(person, p -> getDao().findByNameAndBirthDate(p.getName(), p.getBirthDate()));
	}
}
