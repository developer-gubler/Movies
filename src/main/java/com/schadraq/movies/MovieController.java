package com.schadraq.movies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schadraq.movies.dto.Movie;
import com.schadraq.movies.service.MovieService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieService service;

    @GetMapping("/getByTitle")
    ResponseEntity<List<Movie>> movie(@RequestParam @NotBlank(message = "The title must contain content") String title) {
        return ResponseEntity.ok(service.getByTitle(title));
    }

    @GetMapping("/getByTitleAndReleaseYear")
    ResponseEntity<List<Movie>> movie(@Valid Movie movie) {
        return ResponseEntity.ok(service.get(movie));
    }

    @GetMapping("/list")
    ResponseEntity<List<Movie>> movies() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping("/add")
    ResponseEntity<String> addMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.ok(service.add(movie));
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteMovie(@Valid Movie movie) {
        return ResponseEntity.ok(service.delete(movie));
    }
}
