package com.schadraq.movies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schadraq.movies.dto.Person;
import com.schadraq.movies.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService service;

    @GetMapping("/get")
    ResponseEntity<List<Person>> get(@Valid Person person) {
        return ResponseEntity.ok(service.get(person));
    }

    @GetMapping("/list")
    ResponseEntity<List<Person>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping("/add")
    ResponseEntity<String> add(@Valid @RequestBody Person person) {
        return ResponseEntity.ok(service.add(person));
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> delete(@Valid Person person) {
        return ResponseEntity.ok(service.delete(person));
    }
}
